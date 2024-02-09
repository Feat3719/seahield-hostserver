package com.seahield.hostserver.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seahield.hostserver.config.jwt.TokenProvider;
import com.seahield.hostserver.dto.ArticleDto.ViewMyArticleResponse;
import com.seahield.hostserver.dto.CommentDto.ViewMyCommentsResponse;
import com.seahield.hostserver.dto.UserDto.EditUserInfoRequest;
import com.seahield.hostserver.dto.UserDto.ViewUserInfoResponse;
import com.seahield.hostserver.dto.UserDto.ViewUsersInfoResponse;
import com.seahield.hostserver.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final TokenProvider tokenProvider;

    // 회원 정보 조회
    @GetMapping("/info")
    public ResponseEntity<ViewUserInfoResponse> viewUserInfo(@RequestHeader("Authorization") String accessToken) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserInfo(accessToken));
    }

    // 회원 정보 수정
    @PatchMapping("/info")
    public ResponseEntity<?> editUserInfo(@RequestHeader("Authorization") String accessToken,
            @RequestBody EditUserInfoRequest EditUserInfoRequest) {
        String userId = tokenProvider.getUserId(accessToken);
        userService.editUserInfo(userId, EditUserInfoRequest);
        return ResponseEntity.status(HttpStatus.OK).body("SUCCESS TO EDIT");
    }

    // 내가 작성한 글 조회
    @GetMapping("/articles")
    public ResponseEntity<List<ViewMyArticleResponse>> getUserArticles(
            @RequestHeader("Authorization") String accessToken) {
        String userId = tokenProvider.getUserId(accessToken); // TokenProvider를 통해 userId를 추출하는 로직을 구현해야 함
        List<ViewMyArticleResponse> articles = userService.getUserArticles(userId);
        return ResponseEntity.status(HttpStatus.OK).body(articles);
    }

    // 내가 작성한 댓글 조회
    @GetMapping("/comments")
    public ResponseEntity<List<ViewMyCommentsResponse>> getUserComments(
            @RequestHeader("Authorization") String accessToken) {
        String userId = tokenProvider.getUserId(accessToken); // TokenProvider를 통해 userId를 추출하는 로직을 구현해야 함
        List<ViewMyCommentsResponse> comments = userService.getUserComments(userId);
        return ResponseEntity.status(HttpStatus.OK).body(comments);
    }

    // 내가 좋아요한 글 조회
    @GetMapping("/articles-like")
    public ResponseEntity<List<ViewMyArticleResponse>> getUserArticlesLike(
            @RequestHeader("Authorization") String accessToken) {
        String userId = tokenProvider.getUserId(accessToken); // TokenProvider를 통해 userId를 추출하는 로직을 구현해야 함
        List<ViewMyArticleResponse> articles = userService.getUserLikesArticles(userId);
        return ResponseEntity.status(HttpStatus.OK).body(articles);
    }

    // 회원 정보 전체 조회(관리자권한)
    @GetMapping("/users-info")
    public ResponseEntity<List<ViewUsersInfoResponse>> getAllUsersInfo(
            @RequestHeader("Authorization") String accessToken) {
        String userId = tokenProvider.getUserId(accessToken);
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsersInfo(userId));
    }

}
