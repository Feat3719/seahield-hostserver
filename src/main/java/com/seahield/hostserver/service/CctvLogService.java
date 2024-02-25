package com.seahield.hostserver.service;

import org.springframework.stereotype.Service;

import com.seahield.hostserver.domain.CctvLog;
import com.seahield.hostserver.dto.CctvLogDto.ViewCctvLogDetailsResponse;
import com.seahield.hostserver.dto.CctvLogDto.ViewCctvLogResponse;
import com.seahield.hostserver.exception.ErrorException;
import com.seahield.hostserver.repository.CctvLogRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CctvLogService {

    private final CctvLogRepository cctvLogRepository;

    // 1번 CCTV 로그 간략 조회
    public ViewCctvLogResponse getLatestCctvLogsDynamic() {
        CctvLog cctvlog = cctvLogRepository.findLatestLogByCctvId()
                .orElseThrow(() -> new ErrorException("NOT FOUND LOG"));

        return new ViewCctvLogResponse(
                cctvlog.getCctvLogId(),
                cctvlog.getDetectedDate(),
                cctvlog.getCctv().getCctvId(),
                cctvlog.getObjectCount(),
                cctvlog.getRiskIndex());
    }

    // 특정 CCTV 로그 상세 조회
    public ViewCctvLogDetailsResponse getLatestCctvLogDetailsDynamic(Long cctvLogId) {
        CctvLog latestLog = cctvLogRepository.findCctvLogById(cctvLogId)
                .orElseThrow(() -> new ErrorException("NOT FOUND LOG"));
        return new ViewCctvLogDetailsResponse(
                latestLog.getCctvLogId(),
                latestLog.getDetectedDate(),
                latestLog.getCctv().getCctvId(),
                latestLog.getObjectCount(),
                latestLog.getRiskIndex(),
                latestLog.getPetBottlePer(),
                latestLog.getPlasticEtcPer(),
                latestLog.getMetalPer(),
                latestLog.getGlassPer(),
                latestLog.getNetPer(),
                latestLog.getRopePer(),
                latestLog.getPlasticBuoyChinaPer(),
                latestLog.getPlasticBuoyPer(),
                latestLog.getStyrofoamPiecePer(),
                latestLog.getStyrofoamBuoyPer(),
                latestLog.getStyrofoamBoxPer(),
                latestLog.getPetBottleCnt(),
                latestLog.getPlasticEtcCnt(),
                latestLog.getMetalCnt(),
                latestLog.getGlassCnt(),
                latestLog.getNetCnt(),
                latestLog.getRopeCnt(),
                latestLog.getPlasticBuoyChinaCnt(),
                latestLog.getPlasticBuoyCnt(),
                latestLog.getStyrofoamPieceCnt(),
                latestLog.getStyrofoamBuoyCnt(),
                latestLog.getStyrofoamBoxCnt());
    }

    // 2번 ~ 10번 CCTV 로그 간략 조회
    public List<ViewCctvLogResponse> getLatestCctvLogsStatic(String cctvId) {
        return cctvLogRepository.findCctvLogsByCctvId(cctvId)
                .orElseThrow(() -> new ErrorException("NOT FOUND LOG"));
    }

    // 2번 ~ 10번 CCTV 로그 상세 조회
    public List<ViewCctvLogDetailsResponse> getLatestCctvLogsDetailsStatic(String cctvId) {

        return cctvLogRepository.findCctvLogsDetailsByCctvId(cctvId)
                .orElseThrow(() -> new ErrorException("NOT FOUND LOG"));
    }

}
