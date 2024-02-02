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
import lombok.Builder.Default;

@Table(name = "QNA_COMMENT")
@Entity
@Getter
@Builder
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QnaComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "qna_comment_id", nullable = false) // 댓글 ID
    private Long qnaCommentId;

    @NonNull
    @Column(name = "qna_comment_contents", nullable = false) // 댓글 내용
    private String qnaCommentContents;

    @CreatedDate
    @Column(name = "qna_comment_created_date") // 댓글 생성 날짜
    private LocalDateTime qnaCommentCreatedDate;

    @LastModifiedDate
    @Column(name = "qna_comment_updated_date") // 댓글 업데이트 날짜
    private LocalDateTime qnaCommentUpdatedDate;

    @Default
    @Column(name = "qna_comment_like_counts") // 좋아요 수
    @ColumnDefault("0")
    private Long qnaCommentLikeCounts = 0L;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qna_comment_writer", nullable = false)
    private User qnaCommentWriter;

}
