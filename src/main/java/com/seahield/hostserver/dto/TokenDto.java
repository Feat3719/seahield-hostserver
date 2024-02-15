package com.seahield.hostserver.dto;

import java.time.Duration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class TokenDto {

    @Getter
    @Setter
    public static class CreateAccessTokenRequest {
        private String refreshToken;
    }

    @Getter
    @AllArgsConstructor
    public static class CreateAccessTokenResponse {
        private String accessToken;

    }

    @Getter
    @Builder
    public static class CreateTokensResponse {

        private String refreshToken;
        private String accessToken;
        private Duration expiresIn;

    }

    // 로그아웃
    @Getter
    public static class GetRefreshToken {
        private String accessToken;
    }
}
