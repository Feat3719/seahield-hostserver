package com.seahield.hostserver.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.seahield.hostserver.dto.TokenDto.CreateAccessTokenRequest;
import com.seahield.hostserver.dto.TokenDto.CreateAccessTokenResponse;
import com.seahield.hostserver.dto.TokenDto.CreateTokensResponse;
import com.seahield.hostserver.dto.UserDto.DeleteUserRequest;
import com.seahield.hostserver.dto.UserDto.SignInRequest;
import com.seahield.hostserver.dto.UserDto.SignUpRequest;
import com.seahield.hostserver.exception.ErrorException;
import com.seahield.hostserver.exception.SuccessException;
import com.seahield.hostserver.service.AuthService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // AccessToken 발급 : RefreshToken 을 보유하고 있고 AccessToken 이 없는 경우
    @PostMapping("/token")
    public ResponseEntity<CreateAccessTokenResponse> createNewAccessToken(
            @RequestBody CreateAccessTokenRequest request) {
        String newAccessToken = authService.createNewAccessToken(request.getRefreshToken());
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateAccessTokenResponse(newAccessToken));
    }

    // 로그인
    @PostMapping("/signin")
    public ResponseEntity<?> userSignIn(@RequestBody SignInRequest signInRequest,
            HttpServletResponse response) {
        CreateTokensResponse tokensResponse = authService.signIn(signInRequest);
        String refreshToken = tokensResponse.getRefreshToken();
        String accessToken = tokensResponse.getAccessToken();
        ResponseCookie cookie = ResponseCookie
                .from("refreshToken", refreshToken)
                .path("/")
                .httpOnly(true)
                .secure(true) // HTTPS 환경에서만 사용
                .maxAge(24 * 60 * 60) // 쿠키 유효 시간 (예: 1일)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Set-Cookie", cookie.toString())
                .body(new CreateAccessTokenResponse(accessToken));
    }

    // 아이디 중복 확인
    @GetMapping("/check-avilability-userid")
    public ResponseEntity<?> getUserId(@RequestParam String userId) {
        boolean isAvailable = authService.isUserIdAvailable(userId);
        return ResponseEntity.status(HttpStatus.OK).body(isAvailable);
    }

    // 회원가입
    @PostMapping("/user")
    public ResponseEntity<?> userSignUp(@RequestBody SignUpRequest signUpRequest) {
        authService.save(signUpRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessException("SUCCESS : SignUp"));
    }

    // 로그아웃
    // @PostMapping("/signout")
    // public ResponseEntity<?> userSignOut(@RequestBody GetRefreshToken request) {
    // authService.userSignOut(request);
    // return ResponseEntity.status(HttpStatus.OK).body(new
    // SuccessException("SUCCESS : SignOut"));
    // }

    // 로그아웃
    @PostMapping("/signout")
    public ResponseEntity<?> userSignOut(HttpServletRequest request) {
        String refreshTokenValue = authService.extractRefreshTokenFromCookie(request);
        if (refreshTokenValue != null) {
            authService.userSignOut(refreshTokenValue);
            return ResponseEntity.status(HttpStatus.OK).body(new SuccessException("SUCCESS : SignOut"));
        } else {
            throw new ErrorException("CANNOT FIND RT");
        }
    }

    // 회원 탈퇴
    @DeleteMapping("/user")
    public ResponseEntity<?> deleteUser(@RequestBody DeleteUserRequest request) {
        authService.deleteUser(request);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessException("SUCCESS : Withdraw"));
    }

    // 비밀번호 체크
    @GetMapping("/check-avilability-userpwd")
    public boolean getMethodName(@RequestParam String userId, @RequestParam String userPwd) {
        if (bCryptPasswordEncoder.matches(userPwd, authService.findByUserId(userId).getPassword())) {
            return true;
        }
        return false;
    }

    @GetMapping("/checkrt")
    public void getMethodName(HttpServletRequest request) {
        String refreshTokenValue = null;
        Cookie[] cookies = request.getCookies(); // 쿠키 배열을 받습니다.
        if (cookies != null) {
            for (Cookie cookie : cookies) { // 배열에 대해 반복합니다.
                if ("refreshToken".equals(cookie.getName())) {
                    refreshTokenValue = cookie.getValue();
                    System.out.println("Refresh Token: " + refreshTokenValue);
                    break;
                }
            }
        }
    }

}