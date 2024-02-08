package com.seahield.hostserver.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seahield.hostserver.dto.CompanyDto.CreateCompanyInfoRequest;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/company")
public class CompanyController {

    // 법인 정보 생성 
    @PostMapping("")
    public ResponseEntity<?> postCompany(@RequestBody CreateCompanyInfoRequest companyInfoRequest) {
        
        return ResponseEntity.status(HttpStatus.CREATED).body("SUCCESS TO CREATE");
    }

}
