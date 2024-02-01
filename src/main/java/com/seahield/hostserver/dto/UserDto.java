package com.seahield.hostserver.dto;

import lombok.Getter;
import lombok.Setter;

public class UserDto {

    // 로그인 RequestDto
    @Getter
    public static class SignInRequest {
        private String userId;
        private String userPwd;
    }

    // 회원 가입 Dto
    @Getter
    public static class SignUpRequest {
        private String userId;
        private String userPwd;
        private String userName;
        private String userEmail;
        private String userContact;
        private String userAddress;
        private String userType;
    }

    // 회원가입 문자 인증 발송 RequestDto
    @Getter
    public static class SmsSendRequest {
        private String userContact;
    }

    // 회원가입 문자 인증 확인 RequestDto
    @Getter
    public static class SmsCheckRequest {
        private String userContact;
        private String code;
    }

    // 비밀번호 찾기 RequestDto
    @Getter
    @Setter
    public static class FindUserPwdRequest {
        private String userId;
        private String userEmail;

    }

    // 회원탈퇴 RequestDto
    @Getter
    public static class DeleteUserRequest {
        private String refreshToken;
        private String userPwd;
    }
}
