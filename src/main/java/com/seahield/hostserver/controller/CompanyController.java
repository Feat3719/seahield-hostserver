package com.seahield.hostserver.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seahield.hostserver.config.jwt.TokenProvider;
import com.seahield.hostserver.dto.CompanyDto.CreateCompanyInfoRequest;
import com.seahield.hostserver.dto.CompanyDto.ViewCompanyDefaultInfoResponse;
import com.seahield.hostserver.service.CompanyService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/company")
public class CompanyController {

    private final CompanyService companyService;
    private final TokenProvider tokenProvider;

    // 법인 정보 생성시 기존 정보 불러오기(조회)
    @GetMapping("/default-info")
    public ResponseEntity<ViewCompanyDefaultInfoResponse> getCompanyInfo(
            @RequestHeader("Authorization") String accessToken) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(companyService.viewCompanyDefaultInfo(tokenProvider.getUserId(accessToken)));
    }

    // 법인 정보 생성
    @PostMapping("/info")
    public ResponseEntity<?> postCompany(@RequestBody CreateCompanyInfoRequest companyInfoRequest) {
        companyService.createCompany(companyInfoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("SUCCESS TO CREATE");
    }

    // 법인 정보 최초 여부 (최초면 true, 기존 계약 경험 있으면 false)
    @GetMapping("/validate-info")
    public ResponseEntity<?> checkCompanyInfo(@RequestHeader("Authorization") String accessToken) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(companyService.isCompanyContractFirst(tokenProvider.getUserId(accessToken)));
    }

}
