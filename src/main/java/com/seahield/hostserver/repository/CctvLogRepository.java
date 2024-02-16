package com.seahield.hostserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.seahield.hostserver.domain.CctvLog;
import com.seahield.hostserver.dto.CctvLogDto.ViewCctvLogDetailsResponse;
import com.seahield.hostserver.dto.CctvLogDto.ViewCctvLogResponse;

import java.util.List;
import java.util.Optional;

public interface CctvLogRepository extends JpaRepository<CctvLog, Long> {

    // 1번 CCTV 로그 상세 조회
    @Query("SELECT c FROM CctvLog c WHERE c.detectedDate <= CURRENT_TIMESTAMP ORDER BY c.detectedDate DESC")
    Optional<List<CctvLog>> findLatestLogsDetails();

    // 2번 ~ 10번 로그 간략 조회
    @Query("SELECT new com.seahield.hostserver.dto.CctvLogDto$ViewCctvLogResponse(c.cctvLogId, c.detectedDate, c.cctvId, c.objectCount, c.riskIndex) FROM CctvLog c WHERE c.cctvId = :cctvId")
    Optional<List<ViewCctvLogResponse>> findCctvLogsByCctvId(String cctvId);

    // 2번 ~ 10번 로그 상세 조회
    @Query("SELECT new com.seahield.hostserver.dto.CctvLogDto$ViewCctvLogDetailsResponse(" +
            "c.cctvLogId, c.detectedDate, c.cctvId, c.objectCount, c.riskIndex, " +
            "c.petBottlePer, c.plasticEtcPer, c.metalPer, c.glassPer, c.netPer, " +
            "c.ropePer, c.plasticBuoyChinaPer, c.plasticBuoyPer, c.styrofoamPiecePer, " +
            "c.styrofoamBuoyPer, c.styrofoamBoxPer, c.plasticEtcCnt, c.metalCnt, " +
            "c.glassCnt, c.netCnt, c.ropeCnt, c.plasticBuoyChinaCnt, c.plasticBuoyCnt, " +
            "c.styrofoamPieceCnt, c.styrofoamBuoyCnt, c.styrofoamBoxCnt) " +
            "FROM CctvLog c WHERE c.cctvId = :cctvId")
    Optional<List<ViewCctvLogDetailsResponse>> findCctvLogsDetailsByCctvId(String cctvId);

}
