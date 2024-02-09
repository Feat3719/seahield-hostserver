package com.seahield.hostserver.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seahield.hostserver.config.jwt.TokenProvider;
import com.seahield.hostserver.dto.ContractDto.CreateContractRequest;
import com.seahield.hostserver.dto.ContractDto.ViewContractDetailsResponse;
import com.seahield.hostserver.dto.ContractDto.ViewContractListResponse;
import com.seahield.hostserver.service.ContractService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/contract")
public class ContractController {

    private final ContractService contractService;
    private final TokenProvider tokenProvider;

    // 수거 계약 신청 작성
    @PostMapping("")
    public ResponseEntity<?> postContract(@RequestBody CreateContractRequest createContractRequest) {
        contractService.createContract(createContractRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("SUCCESS TO CREATE");
    }

    // 수거 계약 신청서 목록 조회
    @GetMapping("/list")
    public ResponseEntity<List<ViewContractListResponse>> getContractList(
            @RequestHeader("Authorization") String accessToken) {
        String userId = tokenProvider.getUserId(accessToken);
        return ResponseEntity.status(HttpStatus.OK).body(contractService.viewContractList(userId));
    }

    // 수거 계약 신청서 상세 조회
    @GetMapping("/details/{contractId}")
    public ResponseEntity<ViewContractDetailsResponse> getContractDetails(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable Long contractId) {
        String userId = tokenProvider.getUserId(accessToken);
        return ResponseEntity.status(HttpStatus.OK).body(contractService.viewContractDetails(userId, contractId));
    }

    // 수거 계약 승인 토글
    @PatchMapping("/status/{contractId}/approved")
    public ResponseEntity<?> patchContractStatusApprove(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable Long contractId) {
        String userId = tokenProvider.getUserId(accessToken);
        contractService.updateContractStatusApprove(userId, contractId);
        return ResponseEntity.status(HttpStatus.OK).body("SUCCESS TO SET STATUS APPROVED");
    }

    // 수거 계약 거절 토글
    @PatchMapping("/status/{contractId}/rejected")
    public ResponseEntity<?> patchContractStatusReject(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable Long contractId) {
        String userId = tokenProvider.getUserId(accessToken);
        contractService.updateContractStatusReject(userId, contractId);
        return ResponseEntity.status(HttpStatus.OK).body("SUCCESS TO SET STATUS REJECTED");
    }

}
