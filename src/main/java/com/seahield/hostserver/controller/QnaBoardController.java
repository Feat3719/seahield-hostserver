package com.seahield.hostserver.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seahield.hostserver.domain.QnaArticle;
import com.seahield.hostserver.dto.QnaArticleDto.CreateArticleRequest;
import com.seahield.hostserver.dto.QnaArticleDto.UpdateArticleRequest;
import com.seahield.hostserver.dto.QnaArticleDto.ViewAllArticlesResponse;
import com.seahield.hostserver.dto.QnaArticleDto.ViewArticleResponse;
import com.seahield.hostserver.dto.QnaCommentDto.CreateCommentRequest;
import com.seahield.hostserver.dto.QnaCommentDto.UpdateCommentRequest;
import com.seahield.hostserver.service.QnaArticleService;
import com.seahield.hostserver.service.QnaCommentService;

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

    private final QnaArticleService qnaArticleService;
    private final QnaCommentService qnaCommentService;

    // 게시글 작성
    @PostMapping("/article")
    public ResponseEntity<?> addArticle(@RequestHeader("Authorization") String accessToken,
            @RequestBody CreateArticleRequest createQnaBoardRequest) {
        qnaArticleService.addArticle(accessToken, createQnaBoardRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("SUCCESS CREATE ARTICLE");
    }

    // 게시글 목록 조회(카테고리)
    @GetMapping("/articles")
    public ResponseEntity<List<ViewAllArticlesResponse>> viewAllArticles(
            @RequestParam(name = "page", defaultValue = "1") int page) {
        List<ViewAllArticlesResponse> articles = qnaArticleService.viewAllArticles(page);
        return ResponseEntity.status(HttpStatus.OK).body(articles);
    }

    // 게시글 조회(글 상세)
    @GetMapping("/article/{id}")
    public ResponseEntity<ViewArticleResponse> findArticle(@PathVariable long id) {
        ViewArticleResponse articleResponse = qnaArticleService.viewById(id);
        return ResponseEntity.status(HttpStatus.OK).body(articleResponse);
    }

    // 게시글 삭제
    @DeleteMapping("/article/{id}")
    public ResponseEntity<?> deleteArticle(@PathVariable long id) {
        qnaArticleService.delete(id);
        return ResponseEntity.status(HttpStatus.OK)
                .build();

    }

    // 게시글 수정
    @PatchMapping("/article/{id}")
    public ResponseEntity<?> updateArticle(@PathVariable long id,
            @RequestBody UpdateArticleRequest request) {
        qnaArticleService.updateArticle(id, request);

        return ResponseEntity.status(HttpStatus.OK).body("SUCCESS UPDATE");

    }

    // 게시글 좋아요 +1
    @PostMapping("/article/{id}/like")
    public ResponseEntity<?> increaseLikeCount(@PathVariable Long id) {
        qnaArticleService.increaseLikeCount(id);
        return ResponseEntity.status(HttpStatus.OK).body("SUCCESS TO LIKE ARTICLE");
    }

    // 댓글 작성(CREATE)
    @PostMapping("/comment")
    public ResponseEntity<?> createComment(@RequestHeader("Authorization") String accessToken,
            @RequestBody CreateCommentRequest request) {
        qnaCommentService.addComment(accessToken, request);
        return ResponseEntity.status(HttpStatus.OK).body("SUCCESS TO CREATE");
    }

    // 댓글 수정(UPDATE)
    @PatchMapping("/comment/{id}")
    public ResponseEntity<?> updateComment(@PathVariable long id,
            @RequestBody UpdateCommentRequest request) {
        qnaCommentService.updateComment(id, request);
        return ResponseEntity.status(HttpStatus.OK).body("SUCCESS TO UPDATE");
    }

    // 댓글 삭제(DELETE)
    @DeleteMapping("/comment/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable long id,
            @RequestBody UpdateCommentRequest request) {
        qnaCommentService.deleteComment(id, request);
        return ResponseEntity.status(HttpStatus.OK).body("SUCCESS TO UPDATE");
    }

}
