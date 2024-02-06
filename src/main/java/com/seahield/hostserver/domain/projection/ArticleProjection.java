package com.seahield.hostserver.domain.projection;

import java.time.LocalDateTime;

public interface ArticleProjection {
    Long getArticleId();

    String getArticleCtgr();

    String getArticleTitle();

    LocalDateTime getArticleCreatedDate();

    String getArticleWriterUserId();

    Long getArticleViewCount();

    Long getArticleLikeCount(); // 좋아요 수를 가져오는 메소드
}