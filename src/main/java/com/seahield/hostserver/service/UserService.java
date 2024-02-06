package com.seahield.hostserver.service;

import org.springframework.stereotype.Service;

import com.seahield.hostserver.config.jwt.TokenProvider;
import com.seahield.hostserver.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final TokenProvider tokenProvider;
    private final AuthService authService;

    // 유저 정보 조회
    public User getUserInfo(String accessToken) {
        String userId = tokenProvider.getUserId(accessToken);
        User user = authService.findByUserId(userId);

        return new User(accessToken, accessToken, accessToken, accessToken, accessToken, accessToken, null, accessToken,
                null, null);
    }

}
