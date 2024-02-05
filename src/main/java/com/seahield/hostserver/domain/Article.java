package com.seahield.hostserver.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

@Table(name = "ARTICLE")
@Entity
@Getter
@Builder
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY) // 작성자
    @JoinColumn(name = "article_writer")
    private User articleWriter;

    @Builder.Default
    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY) // 댓글
    private List<Comment> comments = new ArrayList<>();

    public void update(
            String articleCtgr,
            String articleTitle,
            String articleContents) {
        this.articleCtgr = articleCtgr;
        this.articleTitle = articleTitle;
        this.articleContents = articleContents;
    }

    // 게시글 좋아요 Setter
    public void plusQnaArticleLikeCounts(Long articleLikeCounts) {
        this.articleLikeCounts = articleLikeCounts;
    }

    // 좋아요 수를 1 증가시키는 메소드
    public void incrementLikeCount() {
        this.articleLikeCounts++;
    }

    // 좋아요 수를 1 감소시키는 메소드
    public void decrementLikeCount() {
        // 좋아요 수가 0 미만으로 내려가지 않도록 검사
        if (this.articleLikeCounts > 0) {
            this.articleLikeCounts--;
        }
    }

}
