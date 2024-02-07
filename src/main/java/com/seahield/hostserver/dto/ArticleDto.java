package com.seahield.hostserver.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.seahield.hostserver.domain.Article;
import com.seahield.hostserver.domain.User;
import com.seahield.hostserver.dto.CommentDto.ViewCommentResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class ArticleDto {

    // 게시글 작성 RequestDto
    @Getter
    @Builder
    public static class CreateArticleRequest {
        private String articleCtgr;
        private String articleTitle;
        private String articleContents;

        public Article toEntity(User user) {
            return Article.builder()
                    .articleCtgr(articleCtgr)
                    .articleTitle(articleTitle)
                    .articleContents(articleContents)
                    .articleWriter(user)
                    .build();
        }
    }

    // 게시글 목록 조회 ResponseDto
    @Getter
    @AllArgsConstructor
    public static class ViewAllArticlesResponse {
        private Long articleId; // 글 ID
        // private String articleCtgr; // 글 카테고리
        private String articleTitle; // 제목
        private LocalDateTime articleCreatedDate; // 생성일
        private String userId; // 작성자
        private Long articleViewCounts; // 조회수
        private Long articleLikes; // 좋아요 수
    }

    // 게시글 상세 조회 ResponseDto
    @Getter
    @AllArgsConstructor
    public static class ViewArticleResponse {
        private Long articleId; // 글 ID
        private String articleCtgr; // 글 카테고리
        private String articleTitle; // 제목
        private String articleContents; // 내용
        private LocalDateTime articleCreatedDate; // 생성일
        private LocalDateTime articleUpdateDate; // 수정일
        private String userId; // 작성자
        private Long articleViewCount; // 조회수
        private Long articleLikes; // 좋아요수
        private List<ViewCommentResponse> comments; // 댓글
    }

    // 게시글 수정 RequestDto
    @Getter
    @AllArgsConstructor
    public static class UpdateArticleRequest {
        private String articleCtgr; // 글 카테고리
        private String articleTitle; // 제목
        private String articleContents; // 내용
    }

    // 게시물 조회수 증가 Dto
    @Getter
    public static class ArticleViewCountDto {
        private String articleId;
        private Long articleViewCounts;
    }

    // 내가 작성한 글 조회 ResponseDto
    @Getter
    @Builder
    public static class ViewMyArticleResponse {
        private Long articleId;
        private String articleCtgr;
        private String articleTitle;
        private LocalDateTime articleCreatedDate;
    }

}
