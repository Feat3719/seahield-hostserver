package com.seahield.hostserver.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seahield.hostserver.config.jwt.TokenProvider;
import com.seahield.hostserver.domain.QnaArticle;
import com.seahield.hostserver.dto.QnaArticleDto.CreateArticleRequest;
import com.seahield.hostserver.dto.QnaArticleDto.UpdateArticleRequest;
import com.seahield.hostserver.dto.QnaArticleDto.ViewAllArticlesResponse;
import com.seahield.hostserver.dto.QnaArticleDto.ViewArticleResponse;
import com.seahield.hostserver.exception.SuccessException;
import com.seahield.hostserver.service.QnaBoardService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class QnaBoardController {

    private final QnaBoardService qnaBoardService;

    // 게시글 작성
    @PostMapping("/article")
    public ResponseEntity<?> addArticle(@RequestHeader("Authorization") String accessToken,
            @RequestBody CreateArticleRequest createQnaBoardRequest) {
        qnaBoardService.addArticle(accessToken, createQnaBoardRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("SUCCESS CREATE ARTICLE");
    }

    // 게시글 목록 조회(카테고리)
    @GetMapping("/articles")
    public ResponseEntity<List<ViewAllArticlesResponse>> viewAllArticles(
            @RequestParam(name = "page", defaultValue = "1") int page) {
        List<ViewAllArticlesResponse> articles = qnaBoardService.viewAllArticles(page);
        return ResponseEntity.status(HttpStatus.OK).body(articles);
    }

    // 게시글 조회(글 상세)
    @GetMapping("/article/{id}")
    public ResponseEntity<ViewArticleResponse> findArticle(@PathVariable long id) {
        ViewArticleResponse articleResponse = qnaBoardService.viewById(id);
        return ResponseEntity.status(HttpStatus.OK).body(articleResponse);
    }

    // 게시글 삭제
    @DeleteMapping("/article/{id}")
    public ResponseEntity<?> deleteArticle(@PathVariable long id) {
        if (qnaBoardService.delete(id)) {
            return ResponseEntity.status(HttpStatus.OK)
                    .build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .build();
        }
    }

    // 게시글 수정
    @PatchMapping("/article/{id}")
    public ResponseEntity<?> updateArticle(@PathVariable long id,
            @RequestBody UpdateArticleRequest request) {
        qnaBoardService.updateArticle(id, request);

        return ResponseEntity.status(HttpStatus.OK).body("SUCCESS UPDATE");

    }

}
