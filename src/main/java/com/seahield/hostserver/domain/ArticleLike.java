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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "article_like_id", nullable = false)
    private Long articleLikeId;

    @Column(name = "article_like_status", nullable = false)
    private boolean articleLikeStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
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
