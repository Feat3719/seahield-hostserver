package com.seahield.hostserver.domain;

import java.time.LocalDateTime;

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
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Set;
import java.util.HashSet;

@Table(name = "COMMENT", indexes = {
        @Index(name = "idx_comment_id", columnList = "comment_id", unique = true)
})
@Entity
@Getter
@Builder
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "comment_id", nullable = false) // 댓글 ID
    private Long commentId;

    @NonNull
    @Column(name = "comment_contents", nullable = false) // 댓글 내용
    private String commentContents;

    @CreatedDate
    @Column(name = "comment_created_date") // 댓글 생성 날짜
    private LocalDateTime commentCreatedDate;

    @LastModifiedDate
    @Column(name = "comment_updated_date") // 댓글 업데이트 날짜
    private LocalDateTime commentUpdatedDate;

    @Default
    @Column(name = "comment_like_counts") // 글 좋아요 수
    @ColumnDefault("0")
    private Long commentLikeCounts = 0L;

    @Builder.Default
    @OneToMany(mappedBy = "comment", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CommentLike> commentLikes = new HashSet<>();

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_writer", nullable = false)
    private User commentWriter;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id") // 외래키 칼럼 이름 지정
    private Article article;

    // 댓글 수정
    public void update(
            String commentContents) {
        this.commentContents = commentContents;
    }

    // 댓글 좋아요 수를 계산하는 메소드
    public Long getLikeCount() {
        return Long.valueOf(commentLikes.size());
    }

    // 좋아요 수를 업데이트하는 메소드
    public void updateCommentLikeCounts() {
        this.commentLikeCounts = commentLikes.stream()
                .filter(CommentLike::isCommentLikeStatus) // 좋아요 상태가 true인 항목 필터링
                .count(); // 필터링된 항목의 수를 반환
    }
}
