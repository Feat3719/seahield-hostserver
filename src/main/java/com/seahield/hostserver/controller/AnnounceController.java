package com.seahield.hostserver.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seahield.hostserver.dto.AnnounceDto.CreateAnnounceRequest;
import com.seahield.hostserver.dto.AnnounceDto.ViewAnnounceInApply;
import com.seahield.hostserver.dto.AnnounceDto.ViewAnnounceInCtgr;
import com.seahield.hostserver.service.AnnounceService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/announce")
public class AnnounceController {

    private final AnnounceService announceService;

    // 수거 신청서에서 공고 조회
    @GetMapping("/in-apply")
    public ResponseEntity<List<ViewAnnounceInApply>> getAnnounceInApply() {
        List<ViewAnnounceInApply> announces = announceService.getAnnounceInApply();
        return ResponseEntity.status(HttpStatus.OK).body(announces);
    }

    // 공고 게시판에서 공고 조회
    @GetMapping("/in-ctgr/{announceId}")
    public ResponseEntity<ViewAnnounceInCtgr> getAnnounceInCtgr(
            @PathVariable String announceId) {
        ViewAnnounceInCtgr announces = announceService.getAnnounceInCtgr(announceId);
        return ResponseEntity.status(HttpStatus.OK).body(announces);
    }

    // 공고 작성
    @PostMapping("/")
    public ResponseEntity<?> postAnnounce(@RequestHeader("Authorization") String accessToken,
            @RequestBody CreateAnnounceRequest createAnnounceRequest) {
        announceService.CreateAnnounce(accessToken, createAnnounceRequest);
        return ResponseEntity.status(HttpStatus.OK).body("SUCCESS TO CREATE ANNOUNCE");
    }

    // 공고 삭제
    @DeleteMapping("/{announceId}")
    public ResponseEntity<?> deleteAnnounce(@RequestHeader("Authorization") String accessToken,
            @PathVariable String announceId) {
        announceService.DeleteAnnnounce(accessToken, announceId);
        return ResponseEntity.status(HttpStatus.OK).body("SUCCESS TO CREATE ANNOUNCE");
    }

}
