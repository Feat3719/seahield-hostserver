package com.seahield.hostserver.controller;

import java.time.Duration;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.seahield.hostserver.config.jwt.TokenProvider;
import com.seahield.hostserver.domain.UserType;
import com.seahield.hostserver.dto.TokenDto.CreateAccessTokenResponse;
import com.seahield.hostserver.dto.TokenDto.CreateTokensResponse;
import com.seahield.hostserver.dto.UserDto.CRNRequest;
import com.seahield.hostserver.dto.UserDto.DeleteUserRequest;
import com.seahield.hostserver.dto.UserDto.SignInRequest;
import com.seahield.hostserver.dto.UserDto.SignInResponse;
import com.seahield.hostserver.dto.UserDto.SignUpRequest;
import com.seahield.hostserver.exception.ErrorException;
import com.seahield.hostserver.service.AuthService;
import com.seahield.hostserver.service.TokenService;
import com.seahield.hostserver.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final TokenService tokenService;
    private final TokenProvider tokenProvider;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // AccessToken 발급 : RefreshToken 을 보유하고 있고 AccessToken 이 없는 경우
    @PostMapping("/token")
    public ResponseEntity<CreateAccessTokenResponse> createNewAccessToken(HttpServletRequest httpServletRequest) {
        String refreshToken = tokenService.extractRefreshTokenFromCookie(httpServletRequest);
        String newAccessToken = tokenService.createNewAccessToken(refreshToken);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateAccessTokenResponse(newAccessToken));
    }

    // 로그인
    @PostMapping("/signin")
    public ResponseEntity<?> userSignIn(@RequestBody SignInRequest signInRequest,
            HttpServletResponse httpServletRequest) {
        CreateTokensResponse tokensResponse = authService.signIn(signInRequest);
        String refreshToken = tokensResponse.getRefreshToken();
        String accessToken = tokensResponse.getAccessToken();
        Duration expiresIn = tokensResponse.getExpiresIn();
        UserType userType = userService.findByUserId(signInRequest.getUserId()).getUserType();
        ResponseCookie cookie = ResponseCookie
                .from("refreshToken", refreshToken)
                .path("/")
                .httpOnly(true)
                .sameSite("Lax")
                .secure(true) // HTTPS 환경에서만 사용
                .maxAge(24 * 60 * 60) // 쿠키 유효 시간 (예: 1일)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Set-Cookie", cookie.toString())
                .body(new SignInResponse(accessToken, userType, expiresIn));
    }

    // 아이디 중복 확인
    @GetMapping("/check-avilability-userid")
    public ResponseEntity<?> getUserId(@RequestParam String userId) {
        boolean isAvailable = authService.checkUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(isAvailable);
    }

    // 회원가입
    @PostMapping("/user")
    public ResponseEntity<?> userSignUp(@RequestBody SignUpRequest signUpRequest) {

        authService.signUp(signUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("SUCCESS : SignUp");
    }

    // 로그아웃
    @PostMapping("/signout")
    public ResponseEntity<?> userSignOut(HttpServletRequest httpServletRequest) {
        String refreshTokenValue = tokenService.extractRefreshTokenFromCookie(httpServletRequest);
        if (refreshTokenValue != null) {
            authService.userSignOut(refreshTokenValue);
            ResponseCookie cookie = ResponseCookie
                    .from("refreshToken", null)
                    .path("/")
                    .httpOnly(true)
                    .sameSite("Lax")
                    .secure(true) // HTTPS 환경에서만 사용
                    .maxAge(0) // 쿠키 유효 시간 (예: 1일)
                    .build();
            return ResponseEntity.status(HttpStatus.OK)
                    .header("Set-Cookie", cookie.toString())
                    .body("SUCCESS : SignOut");
        } else {
            throw new ErrorException("CANNOT FIND RT");
        }
    }

    // 회원 탈퇴
    @DeleteMapping("/user")
    public ResponseEntity<?> deleteUser(HttpServletRequest httpServletRequest,
            @RequestBody DeleteUserRequest request) {
        authService.deleteUser(httpServletRequest, request);
        return ResponseEntity.status(HttpStatus.OK).body("SUCCESS : Withdraw");
    }

    // 비밀번호 체크
    @GetMapping("/check-availability-userpwd")
    public ResponseEntity<?> getMethodName(@RequestHeader("Authorization") String accessToken,
            @RequestParam String userPwd) {
        String userId = tokenProvider.getUserId(accessToken);
        if (bCryptPasswordEncoder.matches(userPwd,
                userService.findByUserId(userId).getPassword())) {
            return ResponseEntity.status(HttpStatus.OK).body("CORRECT MATCH");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("NOT MATCH");
    }

    // 사업자등록번호 중복검사
    @PostMapping("/validate-crnumber")
    public ResponseEntity<?> isCodeValid(@RequestBody CRNRequest request) {
        boolean isValid = authService.validateCRN(request.getCrn());
        return ResponseEntity.status(HttpStatus.OK).body(isValid);
    }

    // // 관리자용 : RefreshToken 확인하기
    // @GetMapping("/checkrt")
    // public void getMethodName(HttpServletRequest request) {
    // String refreshTokenValue = null;
    // Cookie[] cookies = request.getCookies(); // 쿠키 배열을 받습니다.
    // if (cookies != null) {
    // for (Cookie cookie : cookies) { // 배열에 대해 반복합니다.
    // if ("refreshToken".equals(cookie.getName())) {
    // refreshTokenValue = cookie.getValue();
    // System.out.println("Refresh Token: " + refreshTokenValue);
    // break;
    // }
    // }
    // }
    // }

    // // 관리자용 : AT 로 UserId 추출
    // @PostMapping("/checkuserid")
    // public ResponseEntity<?> getMethodName(@RequestHeader("Authorization") String
    // accessToken) {
    // String userId = tokenProvider.getUserId(accessToken);
    // return ResponseEntity.status(HttpStatus.OK).body(userId);
    // }

}
