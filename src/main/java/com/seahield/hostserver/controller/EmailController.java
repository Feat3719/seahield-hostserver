package com.seahield.hostserver.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.seahield.hostserver.dto.EmailDto.Email;

import com.seahield.hostserver.service.AuthService;
import com.seahield.hostserver.service.EmailService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/email")
public class EmailController {

    private final EmailService emailService;
    private final AuthService authService;

    // 회원가입 인증 코드 메일 전송(회원가입)
    // @PostMapping("/auth-signup")
    // public ResponseEntity<?> sendJoinMail(@RequestBody EmailPostRequest request)
    // {
    // if (!authService.checkNewUserEmail(request.getUserEmail())) {
    // throw new ErrorException("Already Our Member");
    // }
    // Email email = Email.builder()
    // .to(request.getUserEmail())
    // .subject("[Seahield] 회원가입 인증을 위한 인증 코드 발송 메일입니다.")
    // .build();

    // String code = emailService.sendMail(email, "email");

    // EmailViewResponse emailViewResponse = new EmailViewResponse();
    // emailViewResponse.setCode(code);

    // return ResponseEntity.status(HttpStatus.OK).body(emailViewResponse);
    // }

    // 아이디 찾기(메일 전송)d
    @GetMapping("/userid")
    public ResponseEntity<?> findUserId(@RequestParam String userEmail) {

        Email email = Email.builder()
                .to(userEmail)
                .subject("[Seahield] 아이디 찾기 인증 메일입니다.")
                .build();

        emailService.sendAuthEmail(userEmail, email, "idfind");

        return ResponseEntity.status(HttpStatus.OK).body("SUCCESS : ID Found Done");
    }

    // 비밀번호 찾기(임시 비밀번호 발급 메일 전송)
    @GetMapping("/userpwd")
    public ResponseEntity<?> sendPasswordMail(@RequestParam String userId, @RequestParam String userEmail) {

        Email email = Email.builder()
                .to(authService.findUserPwd(userId, userEmail).getUserEmail())
                .subject("[Seahield] 임시 비밀번호 발급 메일입니다.")
                .build();

        emailService.sendMail(email, "password");

        return ResponseEntity.status(HttpStatus.OK).body("SUCCESS : Initialize and reissue password completed");
    }

}
