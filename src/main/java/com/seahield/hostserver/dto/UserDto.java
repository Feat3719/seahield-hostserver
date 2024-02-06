package com.seahield.hostserver.dto;

import java.util.List;

import com.seahield.hostserver.domain.Article;
import com.seahield.hostserver.domain.Comment;
import com.seahield.hostserver.domain.UserType;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

public class UserDto {

    // 로그인 RequestDto
    @Getter
    public static class SignInRequest {
        private String userId;
        private String userPwd;
    }

    // 로그인 ResponseDto
    @Getter
    public static class SignInResponse {
        private String accessToken;
        private UserType userType;

        public SignInResponse(String accessToken, UserType userType) {
            this.accessToken = accessToken;
            this.userType = userType;
        }
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
        private String companyRegistNum;
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
        private String userPwd;
    }

    // 사업자 등록번호 인증 요청 RequestDto
    @Getter
    @Setter
    public static class CRNRequest {
        private String crn;

    }

    // 댓글 작성자 조회 ResponseDto
    @Getter
    public static class ViewCommentWriterResponse {
        private String userId;

        public ViewCommentWriterResponse(String userId) {
            this.userId = userId;
        }
    }

    // 사용자 정보 조회 ResponseDto
    @Getter
    public static class ViewUserInfoResponse {
        private String userId;
        private String userName;
        private String userEmail;
        private String userContact;
        private String userAddress;
        private String userType;
        private String companyRegistNum;
        private LocalDate userJoinedYmd;
        private List<Article> Articles;
        private List<Comment> Comments;

    }
}
