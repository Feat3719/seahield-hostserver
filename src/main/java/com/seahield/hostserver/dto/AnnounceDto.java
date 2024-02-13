package com.seahield.hostserver.dto;

import lombok.Builder;
import lombok.Getter;

public class AnnounceDto {

    @Getter
    @Builder
    public static class ViewAnnounceInApply {
        private String announceId;
        private String announceName;
    }

    @Getter
    @Builder
    public static class ViewAnnounceInCtgr {
        private String announceId;
        private String announceName;
        private String announceContents;
        private String aanounceCreatedDate;
        private String biddingStartDate;
        private String biddingEndDate;
    }
}
