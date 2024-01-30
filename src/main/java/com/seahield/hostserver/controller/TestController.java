package com.seahield.hostserver.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class TestController {

    @GetMapping("/test/{index}")
    public String TestMethod(@PathVariable int index) {
        System.out.println("asdf");
        return "CI/CD Test" + index;
    }

}
