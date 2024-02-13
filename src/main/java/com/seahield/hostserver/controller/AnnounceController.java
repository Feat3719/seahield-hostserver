package com.seahield.hostserver.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seahield.hostserver.dto.AnnounceDto.ViewAnnounceInApply;
import com.seahield.hostserver.dto.AnnounceDto.ViewAnnounceInCtgr;
import com.seahield.hostserver.service.AnnounceService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

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
    @GetMapping("/in-ctgr")
    public ResponseEntity<List<ViewAnnounceInCtgr>> getAnnounceInCtgr() {
        List<ViewAnnounceInCtgr> announces = announceService.getAnnounceInCtgr();
        return ResponseEntity.status(HttpStatus.OK).body(announces);
    }

}
