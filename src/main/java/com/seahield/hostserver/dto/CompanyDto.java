package com.seahield.hostserver.dto;

import lombok.Builder;
import lombok.Getter;

public class CompanyDto {

    // 법인 정보 기존 항목 불러오기(조회) ResponseDto
    @Getter
    @Builder
    public static class ViewCompanyDefaultInfoResponse {
        private String companyRegistNum;
        private String userNickname;
    }

    // 법인 정보 생성 RequestDto
    @Getter
    public static class CreateCompanyInfoRequest {
        private String companyRegistNum;
        private String companyName;
        private String userNickname;
        private String companyAddress;
        private String companyContact;
    }
}
