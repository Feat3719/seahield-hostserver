package com.seahield.hostserver.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seahield.hostserver.domain.User;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    // 사용자 정보 조회
    // @GetMapping("/info")
    // public User viewUserInfo(@RequestParam String param) {
    // User user = new User(param, param, param, param, param, param, null, null,
    // null);
    // return user;
    // }

}
