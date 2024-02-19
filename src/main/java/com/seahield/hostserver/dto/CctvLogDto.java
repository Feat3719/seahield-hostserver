package com.seahield.hostserver.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class CctvLogDto {

    // CCTV 로그 간략 조회 ResponseDto
    @Getter
    @AllArgsConstructor
    public static class ViewCctvLogResponse {
        private Long cctvLogId;
        private LocalDateTime detectedDate;
        private String cctvId;
        private int objectCount;
        private int riskIndex;
    }

    // CCTV 로그 상세 조회 ResponseDto
    @Getter
    @AllArgsConstructor
    public static class ViewCctvLogDetailsResponse {
        private Long cctvLogId;
        private LocalDateTime detectedDate;
        private String cctvId;
        private int objectCount;
        private int riskIndex;
        private float petBottlePer;
        private float plasticEtcPer;
        private float metalPer;
        private float glassPer;
        private float netPer;
        private float ropePer;
        private float plasticBuoyChinaPer;
        private float plasticBuoyPer;
        private float styrofoamPiecePer;
        private float styrofoamBuoyPer;
        private float styrofoamBoxPer;
        private int plasticEtcCnt;
        private int metalCnt;
        private int glassCnt;
        private int netCnt;
        private int ropeCnt;
        private int plasticBuoyChinaCnt;
        private int plasticBuoyCnt;
        private int styrofoamPieceCnt;
        private int styrofoamBuoyCnt;
        private int styrofoamBoxCnt;
    }
}
