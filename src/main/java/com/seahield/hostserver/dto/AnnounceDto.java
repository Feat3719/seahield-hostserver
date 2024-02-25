package com.seahield.hostserver.dto;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDate;

public class AnnounceDto {

    // 수거 신청서에서 공고 조회 ResponseDto
    @Getter
    @Builder
    public static class ViewAnnounceInApply {
        private String announceId;
        private String announceName;
        private LocalDate announceCreatedDate;
    }

    // 공고 게시판에서 공고 조회 ResponseDto
    @Getter
    @Builder
    public static class ViewAnnounceInCtgr {
        private String announceId;
        private String announceName;
        private String announceContents;
        private LocalDate announceCreatedDate;
        private String biddingStartDate;
        private String biddingEndDate;
    }

    // 공고 작성 RequestDto
    @Getter
    public static class CreateAnnounceRequest {
        private String announceId;
        private String announceName;
        private String announceContents;
        private String biddingStartDate;
        private String biddingEndDate;
    }
}
