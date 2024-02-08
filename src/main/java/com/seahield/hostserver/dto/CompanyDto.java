package com.seahield.hostserver.dto;

import lombok.Getter;

public class CompanyDto {

    // 법인 정보 생성 RequestDto
    @Getter
    public static class CreateCompanyInfoRequest {
        private String companyRegistNum;
        private String compnayName;
        private String userNickName;
        private String companyAddress;
        private String companyContact;
    }
}
