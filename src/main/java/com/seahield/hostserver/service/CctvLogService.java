package com.seahield.hostserver.service;

import org.springframework.stereotype.Service;

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
