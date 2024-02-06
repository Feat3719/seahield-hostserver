package com.seahield.hostserver.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seahield.hostserver.domain.Article;
import com.seahield.hostserver.dto.ArticleDto.CreateArticleRequest;
import com.seahield.hostserver.dto.ArticleDto.UpdateArticleRequest;
import com.seahield.hostserver.dto.ArticleDto.ViewAllArticlesResponse;
import com.seahield.hostserver.dto.ArticleDto.ViewArticleResponse;
import com.seahield.hostserver.dto.CommentDto.CreateCommentRequest;
import com.seahield.hostserver.dto.CommentDto.UpdateCommentRequest;
import com.seahield.hostserver.service.BoardArticleService;
import com.seahield.hostserver.service.BoardCommentService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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
public class BoardController {

    private final BoardArticleService articleService;
    private final BoardCommentService commentService;

    // 게시글 작성
    @PostMapping("/article")
    public ResponseEntity<?> addArticle(@RequestHeader("Authorization") String accessToken,
            @RequestBody CreateArticleRequest createQnaBoardRequest) {
        articleService.addArticle(accessToken, createQnaBoardRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("SUCCESS CREATE ARTICLE");
    }

    // 게시글 목록 조회(카테고리)
    @GetMapping("/articles")
    public ResponseEntity<List<ViewAllArticlesResponse>> viewAllArticles(
            @RequestParam(name = "page", defaultValue = "1") int page) {
        List<ViewAllArticlesResponse> articles = articleService.viewAllArticles(page);
        return ResponseEntity.status(HttpStatus.OK).body(articles);
    }

    // 게시글 조회(글 상세)
    @GetMapping("/article/{id}")
    public ResponseEntity<ViewArticleResponse> findArticle(@PathVariable Long id) {
        ViewArticleResponse articleResponse = articleService.viewById(id);
        return ResponseEntity.status(HttpStatus.OK).body(articleResponse);
    }

    // 게시글 삭제
    @DeleteMapping("/article/{id}")
    public ResponseEntity<?> deleteArticle(@PathVariable Long id) {
        articleService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("SUCCESS DELETE");

    }

    // 게시글 수정
    @PatchMapping("/article/{id}")
    public ResponseEntity<?> updateArticle(@PathVariable Long id,
            @RequestBody UpdateArticleRequest request) {
        articleService.updateArticle(id, request);

        return ResponseEntity.status(HttpStatus.OK).body("SUCCESS UPDATE");

    }

    // 게시글 좋아요 기능
    @PostMapping("/article/{id}/like")
    public ResponseEntity<?> toggleLikeArticle(@PathVariable Long id,
            @RequestHeader("Authorization") String accessToken) {
        articleService.toggleLike(accessToken, id);
        return ResponseEntity.status(HttpStatus.OK).body("SUCCESS LIKE ARTICLE");
    }

    // 댓글 작성(CREATE)
    @PostMapping("/comment")
    public ResponseEntity<?> createComment(@RequestHeader("Authorization") String accessToken,
            @RequestBody CreateCommentRequest request) {
        commentService.addComment(accessToken, request);
        return ResponseEntity.status(HttpStatus.OK).body("SUCCESS TO CREATE");
    }

    // 댓글 수정(UPDATE)
    @PatchMapping("/comment/{id}")
    public ResponseEntity<?> updateComment(@PathVariable Long id,
            @RequestBody UpdateCommentRequest request) {
        commentService.updateComment(id, request);
        return ResponseEntity.status(HttpStatus.OK).body("SUCCESS TO UPDATE");
    }

    // 댓글 삭제(DELETE)
    @DeleteMapping("/comment/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.status(HttpStatus.OK).body("SUCCESS TO UPDATE");
    }

    // 댓글 좋아요 기능
    @PostMapping("/comment/{commentId}/like")
    public ResponseEntity<?> toggleCommentLike(@PathVariable Long commentId,
            @RequestHeader("Authorization") String accessToken) {
        commentService.toggleCommentLike(accessToken, commentId);
        return ResponseEntity.status(HttpStatus.OK).body("SUCCESS LIKE COMMENT");
    }
}
