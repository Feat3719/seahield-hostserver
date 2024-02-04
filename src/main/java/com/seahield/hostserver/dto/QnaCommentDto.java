package com.seahield.hostserver.dto;

import java.time.LocalDateTime;

import com.seahield.hostserver.domain.QnaArticle;
import com.seahield.hostserver.domain.QnaComment;
import com.seahield.hostserver.domain.User;
import com.seahield.hostserver.dto.UserDto.ViewCommentWriterResponse;

import lombok.Getter;
import lombok.NonNull;

public class QnaCommentDto {

    // 댓글 생성 RequestDto
    @Getter
    public static class CreateCommentRequest {
        private String qnaCommentContents;
        private Long qnaArticleId;

        public QnaComment toEntity(User user, QnaArticle article) {
            return QnaComment.builder()
                    .qnaCommentContents(qnaCommentContents)
                    .qnaCommentWriter(user)
                    .qnaArticle(article)
                    .build();
        }
    }

    // 댓글 조회 ResponseDto
    @Getter
    public static class ViewCommentResponse {

        private Long qnaCommentId;
        private String qnaCommentContents;
        private LocalDateTime qnaCommentCreatedDate;
        private LocalDateTime qnaCommentUpdatedDate;
        private Long qnaCommentLikeCounts;
        private ViewCommentWriterResponse qnaCommentWriter;

        public ViewCommentResponse(Long qnaCommentId, @NonNull String qnaCommentContents,
                LocalDateTime qnaCommentCreatedDate, LocalDateTime qnaCommentUpdatedDate, Long qnaCommentLikeCounts,
                @NonNull User qnaCommentWriter) {
            this.qnaCommentId = qnaCommentId;
            this.qnaCommentContents = qnaCommentContents;
            this.qnaCommentCreatedDate = qnaCommentCreatedDate;
            this.qnaCommentUpdatedDate = qnaCommentUpdatedDate;
            this.qnaCommentLikeCounts = qnaCommentLikeCounts;
            this.qnaCommentWriter = new ViewCommentWriterResponse(qnaCommentWriter.getUserId());
        }
    }

    // 댓글 수정 ResponseDto
    @Getter
    public static class UpdateCommentRequest {
        private String qnaCommentContents;
    }



}
