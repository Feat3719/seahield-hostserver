package com.seahield.hostserver.dto;

import java.time.LocalDateTime;

import com.seahield.hostserver.domain.Article;
import com.seahield.hostserver.domain.Comment;
import com.seahield.hostserver.domain.CommentLike;
import com.seahield.hostserver.domain.User;
import com.seahield.hostserver.dto.UserDto.ViewCommentWriterResponse;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;

public class CommentDto {

    // 댓글 생성 RequestDto
    @Getter
    public static class CreateCommentRequest {
        private String commentContents;
        private Long articleId;

        public Comment toEntity(User user, Article article) {
            return Comment.builder()
                    .commentContents(commentContents)
                    .commentWriter(user)
                    .article(article)
                    .build();
        }
    }

    // 댓글 조회 ResponseDto
    @Getter
    public static class ViewCommentResponse {

        private Long commentId;
        private String commentContents;
        private LocalDateTime commentCreatedDate;
        private LocalDateTime commentUpdatedDate;
        private Long commentLikes;
        private String userId;

        public ViewCommentResponse(Long commentId, @NonNull String commentContents,
                LocalDateTime commentCreatedDate, LocalDateTime commentUpdatedDate, Long commentLikes,
                @NonNull String userId) {
            this.commentId = commentId;
            this.commentContents = commentContents;
            this.commentCreatedDate = commentCreatedDate;
            this.commentUpdatedDate = commentUpdatedDate;
            this.commentLikes = commentLikes;
            this.userId = userId;
        }
    }

    // 댓글 수정 ResponseDto
    @Getter
    public static class UpdateCommentRequest {
        private String commentContents;
    }

}
