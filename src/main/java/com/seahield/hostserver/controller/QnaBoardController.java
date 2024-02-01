package com.seahield.hostserver.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seahield.hostserver.dto.QnaBoardDto.CreateArticleRequest;
import com.seahield.hostserver.service.QnaBoardService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class QnaBoardController {

    private final QnaBoardService qnaBoardService;

    // 게시글 작성
    @PostMapping("/articles")
    public ResponseEntity<?> addArticle(@RequestBody CreateArticleRequest createQnaBoardRequest) {
        qnaBoardService.addArticle(createQnaBoardRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("SUCCESS CREATED");
    }

    // // 카테고리 조회
    // @GetMapping("/articles")
    // public ResponseEntity<List<>> viewAllArticles() {

    // return new SomeData();
    // }

}
