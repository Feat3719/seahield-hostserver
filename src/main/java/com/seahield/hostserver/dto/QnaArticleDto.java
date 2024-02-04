package com.seahield.hostserver.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.seahield.hostserver.domain.QnaArticle;
import com.seahield.hostserver.domain.User;
import com.seahield.hostserver.dto.QnaCommentDto.ViewCommentResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class QnaArticleDto {

    // 게시글 작성 RequestDto
    @Getter
    @Builder
    public static class CreateArticleRequest {
        private String qnaArticleCtgr;
        private String qnaArticleTitle;
        private String qnaArticleContents;

        public QnaArticle toEntity(User user) {
            return QnaArticle.builder()
                    .qnaArticleCtgr(qnaArticleCtgr)
                    .qnaArticleTitle(qnaArticleTitle)
                    .qnaArticleContents(qnaArticleContents)
                    .qnaArticleWriter(user)
                    .build();
        }
    }

    // 게시글 목록 조회 ResponseDto
    @Getter
    @AllArgsConstructor
    public static class ViewAllArticlesResponse {
        private Long qnaArticleId; // 글 ID
        private String qnaArticleCtgr; // 글 카테고리
        private String qnaArticleTitle; // 제목
        private LocalDateTime qnaArticleCreatedDate; // 생성일
        private String userId; // 작성자
        private Long qnaArticleViewCounts; // 조회수
        private Long qnaArticleLikeCounts; // 좋아요수
    }

    // 게시글 상세 조회 ResponseDto
    @Getter
    @AllArgsConstructor
    public static class ViewArticleResponse {
        private Long qnaArticleId; // 글 ID
        private String qnaArticleCtgr; // 글 카테고리
        private String qnaArticleTitle; // 제목
        private String qnaArticleContents; // 내용
        private LocalDateTime qnaArticleCreatedDate; // 생성일
        private LocalDateTime qnaArticleUpdateDate; // 수정일
        private String userId; // 작성자
        private Long qnaArticleViewCounts; // 조회수
        private Long qnaArticleLikeCounts; // 좋아요수
        private List<ViewCommentResponse> comments; // 댓글
    }

    // 게시글 수정 RequestDto
    @Getter
    @AllArgsConstructor
    public static class UpdateArticleRequest {
        private String qnaArticleCtgr; // 글 카테고리
        private String qnaArticleTitle; // 제목
        private String qnaArticleContents; // 내용
    }

}
