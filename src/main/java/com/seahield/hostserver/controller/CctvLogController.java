package com.seahield.hostserver.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seahield.hostserver.dto.CctvLogDto.ViewCctvLogDetailsResponse;
import com.seahield.hostserver.dto.CctvLogDto.ViewCctvLogResponse;
import com.seahield.hostserver.service.CctvLogService;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cctv")
public class CctvLogController {

    private final CctvLogService cctvLogService;

    // 1번 카메라 로그 간략 조회
    // @GetMapping("/logs-latest")
    // public ResponseEntity<List<ViewCctvLogResponse>> getLatestCctvLogs() {
    // List<ViewCctvLogResponse> latestLogs = cctvLogService.getLatestCctvLogs();
    // return ResponseEntity.status(HttpStatus.OK).body(latestLogs);
    // }

    // 2~10번 카메라 로그 간략 조회
    @GetMapping("/logs/{cctvId}")
    public ResponseEntity<List<ViewCctvLogResponse>> getLatestCctvLogsSecondToTen(@PathVariable String cctvId) {
        List<ViewCctvLogResponse> logs = cctvLogService.getLatestCctvLogsStatic(cctvId);
        return ResponseEntity.status(HttpStatus.OK).body(logs);
    }

    // CCTV 로그 상세 조회
    @GetMapping("/logs-details/{cctvId}")
    public ResponseEntity<List<ViewCctvLogDetailsResponse>> getLatestCctvLogsDetails(@PathVariable String cctvId) {
        List<ViewCctvLogDetailsResponse> logs = cctvLogService.getLatestCctvLogsDetailsStatic(cctvId);
        return ResponseEntity.ok(logs);
    }

}
