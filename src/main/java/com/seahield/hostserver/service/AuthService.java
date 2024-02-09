package com.seahield.hostserver.service;

import java.time.Duration;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.seahield.hostserver.config.jwt.TokenProvider;
import com.seahield.hostserver.domain.Company;
import com.seahield.hostserver.domain.RefreshToken;
import com.seahield.hostserver.domain.User;
import com.seahield.hostserver.domain.UserType;
import com.seahield.hostserver.dto.TokenDto.CreateTokensResponse;
import com.seahield.hostserver.dto.UserDto.DeleteUserRequest;
import com.seahield.hostserver.dto.UserDto.FindUserPwdRequest;
import com.seahield.hostserver.dto.UserDto.SignInRequest;
import com.seahield.hostserver.dto.UserDto.SignUpRequest;
import com.seahield.hostserver.exception.ErrorException;
import com.seahield.hostserver.repository.CompanyRepository;
import com.seahield.hostserver.repository.RefreshTokenRepository;
import com.seahield.hostserver.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final TokenProvider tokenProvider;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TokenService tokenService;
    private final UserService userService;

    // RT로 회원 찾기
    // private User findUserByRefreshToken(String refreshToken) {
    // if (userRepository
    // .findByUserId(refreshTokenRepository.findByRefreshToken(refreshToken).get().getUserId())
    // == null) {
    // throw new ErrorException("CANNOT FIND USER");
    // } else {
    // return userRepository
    // .findByUserId(refreshTokenRepository.findByRefreshToken(refreshToken).get().getUserId());
    // }
    // }

    // 로그인(RT, AT 발급)
    @Transactional
    public CreateTokensResponse signIn(SignInRequest signInRequest) {
        // 아이디와 비밀번호 체크
        User user = userService.findByUserId(signInRequest.getUserId());
        this.verifyPassword(user, signInRequest.getUserPwd());

        // Refresh Token 발급 + DB에 저장
        String refreshToken = tokenProvider.makeRefreshToken(user);
        String accessToken = tokenProvider.generateToken(user, Duration.ofMinutes(30));
        return CreateTokensResponse.builder()
                .refreshToken(refreshToken)
                .accessToken(accessToken)
                .build();
    }

    // 회원가입
    public void signUp(SignUpRequest request) {
        if (!this.checkUserId(request.getUserId())) {
            throw new ErrorException("ID ALREADY EXIST");
        }
        if (!this.checkNewUserEmail(request.getUserEmail())) {
            throw new ErrorException("EMAIL ALREADY EXIST");
        }
        if (!this.checkUserContact(request.getUserContact())) {
            throw new ErrorException("CONTACT ALREADY EXIST");
        }
        this.save(request);
    }

    // 회원가입 - 회원 저장 로직
    @Transactional
    private void save(SignUpRequest signUpRequest) {
        UserType userType = UserType.fromDescription(signUpRequest.getUserType());

        Company company = null;
        // UserType이 "일반"이 아니고, 회사 등록 번호가 제공된 경우에만 Company 객체를 생성 및 저장
        if (!UserType.GENERAL.getDescription().equals(signUpRequest.getUserType())
                && signUpRequest.getCompanyRegistNum() != null) {
            company = this.saveCompany(signUpRequest.getCompanyRegistNum());
        }

        // User 객체 생성
        User user = User.builder()
                .userId(signUpRequest.getUserId())
                .userPwd(bCryptPasswordEncoder.encode(signUpRequest.getUserPwd()))
                .userNickname(signUpRequest.getUserNickname())
                .userEmail(signUpRequest.getUserEmail())
                .userContact(signUpRequest.getUserContact())
                .userAddress(signUpRequest.getUserAddress())
                .userType(userType)
                .company(company) // company가 null인 경우, User와 연관된 Company는 없음
                .build();

        // User 저장
        userRepository.save(user);
    }

    @Transactional
    private Company saveCompany(String companyRegistNum) {
        if (companyRepository.existsByCompanyRegistNum(companyRegistNum)) {
            throw new ErrorException("COMPANY_REGIST_NUM ALREADY EXISTS");
        }
        Company company = new Company(companyRegistNum);
        companyRepository.save(company);
        return company;
    }

    // 비밀번호 검증
    private void verifyPassword(User user, String pwd) {
        if (!bCryptPasswordEncoder.matches(pwd, user.getPassword())) {
            throw new ErrorException("NOT MATCH PASSWORD");
        }
    }

    // 아이디 중복 확인 (이미 있으면 false, 없으면 true)
    public boolean checkUserId(String userId) {
        return !userRepository.existsByUserId(userId);
    }

    // 이메일 중복 확인 (이미 존재하면 false, 없으면 true)
    public boolean checkNewUserEmail(String email) {
        return !userRepository.existsByUserEmail(email);
    }

    // 휴대전화번호 중복 확인 (이미 존재하면 false, 없으면 true)
    private boolean checkUserContact(String userContact) {
        return !userRepository.existsByUserContact(userContact);
    }

    // 비밀번호 찾기(임시 비밀번호 발급 및 설정)
    @Transactional
    public void setTempPassword(String userEmail, String tempPassword) {
        User user = userService.findByUserEmail(userEmail);

        user.updatePassword(bCryptPasswordEncoder.encode(tempPassword));
        userRepository.save(user);
    }

    // 비밀번호 찾기
    public FindUserPwdRequest findUserPwd(String userId, String userEmail) {
        // 아이디 체크
        User user = userService.findByUserId(userId);
        // 이메일 체크
        if (user.getUserEmail().equals(userEmail)) {
            FindUserPwdRequest request = new FindUserPwdRequest();
            request.setUserEmail(userEmail);
            request.setUserId(userId);
            return request;
        } else {
            throw new ErrorException("NOT MATCH EMAIL");
        }
    }

    // 아이디 찾기
    public String findUserId(String userEmail) {
        User user = userService.findByUserEmail(userEmail);

        return user.getUserId();
    }

    // 로그아웃
    public void userSignOut(String refreshTokenValue) {
        RefreshToken refreshToken = tokenService.findByRefreshToken(refreshTokenValue);
        if (refreshToken != null) {
            refreshTokenRepository.delete(refreshToken);
        } else {
            throw new ErrorException("CANNOT DELETE RT");
        }
    }

    // 회원 탈퇴
    @Transactional
    public void deleteUser(HttpServletRequest refreshtokenRequest, DeleteUserRequest request) {
        String refershToken = tokenService.extractRefreshTokenFromCookie(refreshtokenRequest);
        String userId = tokenService.findByRefreshToken(refershToken).getUserId();
        User user = userService.findByUserId(userId);
        if (!bCryptPasswordEncoder.matches(request.getUserPwd(), user.getUserPwd())) {
            throw new ErrorException("INPUT PWD INCORRECT");
        } else {
            refreshTokenRepository.deleteByUserId(userId);
            userRepository.deleteByUserId(userId);
        }
    }

    // 사업자 등록번호 중복검사
    public boolean validateCRN(String crn) {
        boolean isExists = userRepository.existsByCompany_CompanyRegistNum(crn);
        if (isExists) {
            throw new ErrorException("COMPANY_REGIST_NUM ALREADY EXISTS");
        }
        return true;
    }

    // 관리자 권한 검증 (관리자면 true 아니면 false)
    public boolean verifyAdmin(String userId) {
        UserType userType = userService.findByUserId(userId).getUserType();
        if (userType.equals(UserType.ADMIN)) {
            return true;
        }
        return false;
    }

}
