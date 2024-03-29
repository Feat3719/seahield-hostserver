package com.seahield.hostserver.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Builder.Default;

@Table(name = "ARTICLE", indexes = {
        @Index(name = "idx_article_id", columnList = "article_id", unique = true)
})
@Entity
@Getter
@Builder
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article implements Serializable {

    private static final long serialVersionUID = 200L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "article_id", nullable = false) // 글 ID
    private Long articleId;

    @NonNull
    @Column(name = "article_ctgr", nullable = false) // 글 카테고리
    private String articleCtgr;

    @NonNull
    @Column(name = "article_title", nullable = false) // 글 제목
    private String articleTitle;

    @NonNull
    @Column(name = "article_contents", nullable = false) // 글 내용
    private String articleContents;

    @CreatedDate
    @Column(name = "article_created_date") // 글 생성 날짜
    private LocalDateTime articleCreatedDate;

    @LastModifiedDate
    @Column(name = "article_updated_date") // 글 업데이트 날짜
    private LocalDateTime articleUpdatedDate;

    @Default
    @Column(name = "article_view_counts") // 글 조회 수
    @ColumnDefault("0")
    private Long articleViewCounts = 0L;

    @Default
    @Column(name = "article_like_counts") // 글 좋아요 수
    @ColumnDefault("0")
    private Long articleLikeCounts = 0L;

    @Builder.Default
    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true) // 좋아요
    private Set<ArticleLike> articleLikes = new HashSet<>();

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY) // 작성자
    @JoinColumn(name = "article_writer")
    private User articleWriter;

    @Builder.Default
    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE) // 댓글
    private List<Comment> comments = new ArrayList<>();

    // 게시글 수정 Setter
    public void update(
            String articleCtgr,
            String articleTitle,
            String articleContents) {
        this.articleCtgr = articleCtgr;
        this.articleTitle = articleTitle;
        this.articleContents = articleContents;
    }

    // 좋아요 수를 계산하는 메소드
    public Long getArticleLikeCount() {
        // articleLikes 컬렉션을 순회하여 좋아요 상태가 true인 항목의 수를 계산
        return (Long) articleLikes.stream()
                .filter(ArticleLike::isArticleLikeStatus) // 좋아요 상태가 true인 항목 필터링
                .count(); // 필터링된 항목의 수를 반환
    }

    // 좋아요 수를 업데이트하는 메소드
    public void updateArticleLikeCounts() {
        this.articleLikeCounts = articleLikes.stream()
                .filter(ArticleLike::isArticleLikeStatus) // 좋아요 상태가 true인 항목 필터링
                .count(); // 필터링된 항목의 수를 반환
    }

    // 게시물 조회수 Setter
    // public void setArticleViewCounts(Long articleViewCounts) {
    // this.articleViewCounts = articleViewCounts;
    // }

}
