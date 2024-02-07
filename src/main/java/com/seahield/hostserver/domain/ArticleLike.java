package com.seahield.hostserver.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "ARTICLE_LIKE")
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleLike {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "article_like_id", nullable = false) // 게시글 좋아요 관리번호
    private Long articleLikeId;

    @Column(name = "article_like_status", nullable = false) // 게시글 좋아요 상태
    private boolean articleLikeStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // 게시글 좋아요를 누른 사용자
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false) // 게시글
    private Article article;

    public void setArticleLikeStatus(boolean articleLikeStatus) {
        this.articleLikeStatus = articleLikeStatus;
    }

    public ArticleLike(User user, Article article, boolean articleLikeStatus) {
        this.user = user;
        this.article = article;
        this.articleLikeStatus = articleLikeStatus;
    }

}
