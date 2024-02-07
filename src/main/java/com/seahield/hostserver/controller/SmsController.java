package com.seahield.hostserver.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seahield.hostserver.dto.UserDto.SmsCheckRequest;
import com.seahield.hostserver.dto.UserDto.SmsSendRequest;
import com.seahield.hostserver.service.SmsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sms")
public class SmsController {

    private final SmsService smsService;

    // 인증번호 발송
    @PostMapping("/send")
    public ResponseEntity<?> sendSms(@RequestBody SmsSendRequest request) {
        smsService.sendSms(request.getUserContact());
        return ResponseEntity.status(HttpStatus.OK).body("SUCCESS TO SEND");
    }

    // 인증번호 확인
    @PostMapping("/check")
    public ResponseEntity<?> checkSms(@RequestBody SmsCheckRequest request) {
        boolean isCorrect = smsService.checkSms(request.getUserContact(), request.getCode());
        if (isCorrect) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body("SUCCESS : Correct Certification Number");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("ERROR : Incorrect Certification Number");
        }
    }

    // 테스트
    // @PostMapping("/test")
    // public void testName(@RequestBody TestRequest request) {
    // messageCertificationRepository.createTest(request.getTest(),
    // request.getTesttest());
    // }

}
