package com.seahield.hostserver.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seahield.hostserver.config.jwt.TokenProvider;
import com.seahield.hostserver.dto.ContractDto.CreateContractRequest;
import com.seahield.hostserver.dto.ContractDto.ViewContractListResponse;
import com.seahield.hostserver.service.ContractService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    @GetMapping("")
    public ResponseEntity<List<ViewContractListResponse>> getContract(
            @RequestHeader("Authorization") String accessToken) {
        String userId = tokenProvider.getUserId(accessToken);
        return ResponseEntity.status(HttpStatus.OK).body(contractService.getContractList(userId));
    }

}
