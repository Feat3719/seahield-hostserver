package com.seahield.hostserver.domain;

import java.time.LocalDateTime;

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
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Table(name = "QNA_ARTICLE")
@Entity
@Getter
@Builder
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QnaArticle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qna_article_id", nullable = false) // 글 ID
    private long qnaArticleId;

    @NonNull
    @Column(name = "qna_article_ctgr", nullable = false) // 글 카테고리
    private String qnaArticleCtgr;

    @NonNull
    @Column(name = "qna_article_title", nullable = false) // 글 제목
    private String qnaArticleTitle;

    @NonNull
    @Column(name = "qna_article_contents", nullable = false) // 글 내용
    private String qnaArticleContents;

    @CreatedDate
    @Column(name = "qna_article_created_date") // 글 생성 날짜
    private LocalDateTime qnaArticleCreatedDate;

    @LastModifiedDate
    @Column(name = "qna_article_updated_date") // 글 업데이트 날짜
    private LocalDateTime qnaArticleUpdatedDate;

    @Column(name = "qna_article_view_counts") // 글 조회 수
    @ColumnDefault("0")
    private Long qnaArticleViewCounts = 0L;

    @Column(name = "qna_article_like_counts") // 글 좋아요 수
    @ColumnDefault("0")
    private Long qnaArticleLikeCounts = 0L;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY) // 작성자
    @JoinColumn(name = "qna_article_writer")
    private User qnaArticleWriter;

    public void update(
            String qnaArticleCtgr,
            String qnaArticleTitle,
            String qnaArticleContents) {
        this.qnaArticleCtgr = qnaArticleCtgr;
        this.qnaArticleTitle = qnaArticleTitle;
        this.qnaArticleContents = qnaArticleContents;
    }

}
