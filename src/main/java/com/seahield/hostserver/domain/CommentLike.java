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

@Entity
@Table(name = "COMMENT_LIKE")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA는 기본 생성자를 필요로 합니다.
@AllArgsConstructor
public class CommentLike {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "comment_like_id", nullable = false) // 댓글 좋아요 관리번호
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id") // 댓글
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // 댓글 좋아요 누른 사용자
    private User user;

    @Column(name = "comment_like_status", nullable = false) // 댓글 좋아요 상태
    private boolean commentLikeStatus;

    public CommentLike(User user, Comment comment, boolean commentLikeStatus) {
        this.user = user;
        this.comment = comment;
        this.commentLikeStatus = commentLikeStatus;
    }

    public void setCommentLikeStatus(boolean commentLikeStatus) {
        this.commentLikeStatus = commentLikeStatus;
    }
}
