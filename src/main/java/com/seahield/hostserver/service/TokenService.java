package com.seahield.hostserver.service;

import java.time.Duration;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.seahield.hostserver.config.jwt.TokenProvider;
import com.seahield.hostserver.domain.RefreshToken;
import com.seahield.hostserver.domain.User;
import com.seahield.hostserver.dto.TokenDto.CreateAccessTokenResponse;
import com.seahield.hostserver.exception.ErrorException;
import com.seahield.hostserver.repository.RefreshTokenRepository;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenProvider tokenProvider;
    private final UserService userService;
    private final RefreshTokenRepository refreshTokenRepository;

    // 엑세스 토큰 발급
    public CreateAccessTokenResponse createNewAccessToken(String refreshToken) {
        if (!tokenProvider.validToken(refreshToken)) {
            throw new ErrorException("NOT VALID TOKEN");
        }
        String userId = this.findByRefreshToken(refreshToken).getUserId();
        User user = userService.findByUserId(userId);
        Duration expiresIn = Duration.ofMinutes(30);
        return new CreateAccessTokenResponse(tokenProvider.generateToken(user, expiresIn), expiresIn);
    }

    // RT 찾기
    @Cacheable(value = "refreshToken", key = "#refreshToken")
    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new ErrorException("NOT FOUND RT"));
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

}
