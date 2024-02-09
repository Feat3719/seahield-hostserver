package com.seahield.hostserver.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.seahield.hostserver.domain.RefreshToken;
import com.seahield.hostserver.domain.User;
import com.seahield.hostserver.repository.RefreshTokenRepository;
import javax.crypto.SecretKey;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class TokenProvider {

    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(1);
    private final JwtProperties jwtProperties;
    private final RefreshTokenRepository refreshTokenRepository;

    // 토큰생성
    public String generateToken(User user, Duration expiredAt) {
        Date now = new Date();
        return makeToken(new Date(now.getTime() + expiredAt.toMillis()), user);
    }

    private String makeToken(Date expiry, User user) {
        Date now = new Date();
        SecretKey key = Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .setSubject(user.getUserEmail())
                .claim("id", user.getUserId())
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public UsernamePasswordAuthenticationToken getAuthentication(String token) {
        Claims claims = getClaims(token);
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        return new UsernamePasswordAuthenticationToken(
                new org.springframework.security.core.userdetails.User(claims.getSubject(), "", authorities), token,
                authorities);
    }

    // refresh token 생성
    public String makeRefreshToken(User user) {
        String refreshToken = this.generateToken(user, REFRESH_TOKEN_DURATION);
        saveRefreshToken(user.getUserId(), refreshToken);
        return refreshToken;
    }

    // refresh token => DB에 저장
    private void saveRefreshToken(String userId, String newRefreshToken) {
        RefreshToken refreshtoken = refreshTokenRepository.findByUserId(userId)
                .map(entity -> entity.update(newRefreshToken))
                .orElse(new RefreshToken(userId, newRefreshToken));
        refreshTokenRepository.save(refreshtoken);
    }

    // AT로 유저 아이디 추출
    public String getUserId(String token) {
        Claims claims = decodeJwtToken(token);
        return claims.get("id", String.class); // Claim에서 "id" 값을 String으로 안전하게 추출
    }

    // 토큰에서 시작과 끝 따옴표 제거
    private String sanitizeToken(String token) {
        if (token != null) {
            token = token.trim(); // 공백 제거
            if (token.startsWith("\"") && token.endsWith("\"")) {
                token = token.substring(1, token.length() - 1);
            }
            if (token.startsWith("Bearer ")) { // Bearer 토큰 형식 처리
                token = token.substring(7);
            }
        }
        return token;
    }

    private Claims getClaims(String token) {
        SecretKey key = Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
    }

    private Claims decodeJwtToken(String token) {
        token = sanitizeToken(token);
        SecretKey key = getSecretKey();
        return Jwts.parserBuilder()
                .setSigningKey(key) // 중복 제거
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
