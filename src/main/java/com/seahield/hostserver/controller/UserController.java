package com.seahield.hostserver.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seahield.hostserver.dto.UserDto.EditUserInfoRequest;
import com.seahield.hostserver.dto.UserDto.ViewUserInfoResponse;
import com.seahield.hostserver.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    // 회원 정보 조회
    @GetMapping("/info")
    public ResponseEntity<ViewUserInfoResponse> viewUserInfo(@RequestHeader("Authorization") String accessToken) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserInfo(accessToken));
    }

    // 회원 정보 수정
    @PatchMapping("/info")
    public ResponseEntity<?> editUserInfo(@RequestHeader("Authorization") String accessToken,
            @RequestBody EditUserInfoRequest EditUserInfoRequest) {
        userService.editUserInfo(accessToken, EditUserInfoRequest);
        return ResponseEntity.status(HttpStatus.OK).body("SUCCESS TO EDIT");
    }

}
