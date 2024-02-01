package com.seahield.hostserver.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.seahield.hostserver.config.jwt.TokenProvider;
import com.seahield.hostserver.domain.RefreshToken;
import com.seahield.hostserver.domain.User;
import com.seahield.hostserver.dto.TokenDto.CreateTokensResponse;
import com.seahield.hostserver.dto.TokenDto.GetRefreshToken;
import com.seahield.hostserver.dto.UserDto.DeleteUserRequest;
import com.seahield.hostserver.dto.UserDto.FindUserPwdRequest;
import com.seahield.hostserver.dto.UserDto.SignInRequest;
import com.seahield.hostserver.dto.UserDto.SignUpRequest;
import com.seahield.hostserver.exception.ErrorException;
import com.seahield.hostserver.repository.RefreshTokenRepository;
import com.seahield.hostserver.repository.UserRepository;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 엑세스 토큰 발급
    public String createNewAccessToken(String refreshToken) {
        if (!tokenProvider.validToken(refreshToken)) {
            throw new ErrorException("NOT VALID TOKEN");
        }
        String userId = this.findByRefreshToken(refreshToken).getUserId();
        User user = this.findByUserId(userId);

        return tokenProvider.generateToken(user, Duration.ofMinutes(30));
    }

    // RT 찾기
    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new ErrorException("NOT FOUND RT"));
    }

    // RT로 회원 찾기
    private User findUserByRefreshToken(String refreshToken) {
        if (userRepository
                .findByUserId(refreshTokenRepository.findByRefreshToken(refreshToken).get().getUserId()) == null) {
            throw new ErrorException("CANNOT FIND USER");
        } else {
            return userRepository
                    .findByUserId(refreshTokenRepository.findByRefreshToken(refreshToken).get().getUserId());
        }
    }

    // 아이디로 회원 찾기
    public User findByUserId(String userId) {
        if (userRepository.findByUserId(userId) == null) {
            throw new ErrorException("NOT FOUND ID");
        } else {
            return userRepository.findByUserId(userId);
        }
    }

    // 이메일로 회원 찾기
    private User findByUserEmail(String email) {
        if (userRepository.findByUserEmail(email) == null) {
            throw new ErrorException("NOT FOUND EMAIL");
        } else {
            return userRepository.findByUserEmail(email).orElseThrow();
        }
    }

    // 로그인(RT, AT 발급)
    @Transactional
    public CreateTokensResponse signIn(SignInRequest signInRequest) {
        // 아이디와 비밀번호 체크
        User user = this.findByUserId(signInRequest.getUserId());
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
    @Transactional
    public void save(SignUpRequest signUpRequest) {
        User user = User.builder()
                .userId(signUpRequest.getUserId())
                .userPwd(bCryptPasswordEncoder.encode(signUpRequest.getUserPwd()))
                .userName(signUpRequest.getUserName())
                .userEmail(signUpRequest.getUserEmail())
                .userContact(signUpRequest.getUserContact())
                .userAddress(signUpRequest.getUserAddress())
                .userType(signUpRequest.getUserType())
                .userJoinedYmd(LocalDate.now())
                .userUpdateYmd(LocalDateTime.now())
                .build();

        userRepository.save(user);
    }

    // 비밀번호 검증
    private void verifyPassword(User user, String pwd) {
        if (!bCryptPasswordEncoder.matches(pwd, user.getPassword())) {
            throw new ErrorException("NOT MATCH PASSWORD");
        }
    }

    // 사용자 아이디 체크 (입력한 id가 이미 있으면 false, 없으면 true)
    public boolean checkUserId(String userId) {
        if (userRepository.findByUserId(userId) == null) {
            return true;
        } else {
            new ErrorException("ALREADY ID EXISTS");
            return false;

        }

    }

    // 아이디 중복 확인
    public boolean isUserIdAvailable(String userId) {
        return !userRepository.existsByUserId(userId);
    }

    // 이메일 중복 확인
    public boolean checkNewUserEmail(String email) {
        if (userRepository.findByUserEmail(email).isPresent()) {
            return false;
        }
        return true;
    }

    // 비밀번호 찾기(임시 비밀번호 발급 및 설정)
    @Transactional
    public void setTempPassword(String userEmail, String tempPassword) {
        User user = this.findByUserEmail(userEmail);

        user.updatePassword(bCryptPasswordEncoder.encode(tempPassword));
        userRepository.save(user);
    }

    // 비밀번호 찾기
    public FindUserPwdRequest findUserPwd(String userId, String userEmail) {
        // 아이디 체크
        User user = this.findByUserId(userId);
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
        User user = this.findByUserEmail(userEmail);

        return user.getUserId();
    }

    // 로그아웃
    public void userSignOut(String refreshTokenValue) {
        RefreshToken refreshToken = this.findByRefreshToken(refreshTokenValue);
        if (refreshToken != null) {
            refreshTokenRepository.delete(refreshToken);
        } else {
            throw new ErrorException("CANNOT DELETE RT");
        }
    }

    // 쿠키에서 RT 찾기
    public String extractRefreshTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    // 회원 탈퇴
    @Transactional
    public void deleteUser(DeleteUserRequest request) {
        String userId = this.findUserByRefreshToken(request.getRefreshToken()).getUserId();
        refreshTokenRepository.deleteByUserId(userId);
    }

}
