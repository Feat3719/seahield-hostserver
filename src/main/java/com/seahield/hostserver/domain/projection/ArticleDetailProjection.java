package com.seahield.hostserver.domain.projection;

import java.time.LocalDateTime;
import java.util.List;

import com.seahield.hostserver.dto.CommentDto.ViewCommentResponse;

public interface ArticleDetailProjection {
    Long getArticleId();

    String getArticleCtgr();

    String getArticleTitle();

    String getArticleContents();

    LocalDateTime getArticleCreatedDate();

    LocalDateTime getArticleUpdatedDate();

    String getArticleWriterUserId();

    Long getArticleViewCount();

    Long getArticleLikeCount();

    List<ViewCommentResponse> getComments();

}