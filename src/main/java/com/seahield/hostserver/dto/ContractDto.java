package com.seahield.hostserver.dto;


import lombok.Builder;
import lombok.Getter;

public class ContractDto {

    // 수거 계약 신청(최초) RequestDto
    @Getter
    public static class CreateContractRequest {
        private String contractAplDate; // 계약신청일자
        private Long contractPrice; // 계약입찰금액
        private String announceId; // 공고 번호
        private String annoucneName; // 공고 내용
        private String companyRegistNum; // 사업자 등록번호
        private String companyName; // 법인 명
        private String companyAddress; // 법인 주소
        private String companyContact; // 법인 연락처
        private String userNickName; // ceo 이름
    }

    // 수거 계약 신청서 목록 조회 ResponseDto
    @Getter
    @Builder
    public static class ViewContractListResponse {
        private Long contractId; // 계약 신청서 번호
        private String contractAplDate; // 계약신청일자
        private String announceId; // 공고 번호
        private String companyName; // 법인 명
    }

}
