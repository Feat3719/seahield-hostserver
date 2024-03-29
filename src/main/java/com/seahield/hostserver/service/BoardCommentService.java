package com.seahield.hostserver.service;

import java.util.Optional;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.seahield.hostserver.config.jwt.TokenProvider;
import com.seahield.hostserver.domain.Article;
import com.seahield.hostserver.domain.Comment;
import com.seahield.hostserver.domain.CommentLike;
import com.seahield.hostserver.domain.User;
import com.seahield.hostserver.dto.CommentDto.CreateCommentRequest;
import com.seahield.hostserver.dto.CommentDto.UpdateCommentRequest;
import com.seahield.hostserver.exception.ErrorException;
import com.seahield.hostserver.repository.CommentLikeRepository;
import com.seahield.hostserver.repository.CommentRepository;
import com.seahield.hostserver.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardCommentService {

    private final TokenProvider tokenProvider;
    private final BoardArticleService boardArticleService;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final UserRepository userRepository;

    // 댓글 생성(CREATE)
    @Transactional
    public Comment addComment(String accessToken, CreateCommentRequest request) {
        String userId = tokenProvider.getUserId(accessToken);
        User user = this.findByUserId(userId);
        Article article = boardArticleService.findArticleByArticleId(request.getArticleId());
        return commentRepository.save(request.toEntity(user, article));
    }

    // 댓글 수정(UPDATE)
    @Transactional
    public void updateComment(Long id, UpdateCommentRequest request) {
        Comment comment = this.findCommentByCommentId(id);
        comment.update(request.getCommentContents());
    }

    // 댓글 삭제(DELETE)
    @Transactional
    public void deleteComment(Long id) {
        Comment comment = this.findCommentByCommentId(id);
        commentRepository.deleteByCommentId(comment.getCommentId());
    }

    // 댓글 ID 로 댓글 찾기
    private Comment findCommentByCommentId(Long qnaCommentId) {
        return commentRepository.findByCommentId(qnaCommentId)
                .orElseThrow(() -> new ErrorException("NOT FOUND COMMENT ID : " + qnaCommentId));
    }

    // 유저ID 로 댓글 찾기
    public List<Comment> findCommentByUserId(String userId) {
        User user = this.findByUserId(userId);
        return commentRepository.findByCommentWriter(user).orElseThrow(null);
    }

    // 댓글 좋아요 토글 메소드
    @Transactional
    public void toggleCommentLike(String accessToken, Long commentId) {
        String userId = tokenProvider.getUserId(accessToken);
        User user = this.findByUserId(userId);
        Comment comment = this.findCommentByCommentId(commentId);

        Optional<CommentLike> commentLikeOpt = commentLikeRepository.findByUserAndComment(user, comment);

        if (commentLikeOpt.isPresent()) {
            // 좋아요가 이미 존재한다면, 해당 좋아요를 삭제합니다.
            CommentLike commentLike = commentLikeOpt.get();
            comment.getCommentLikes().remove(commentLike);
            commentLikeRepository.delete(commentLike);
        } else {
            // 좋아요가 존재하지 않는다면, 새로운 좋아요를 추가합니다.
            CommentLike commentLike = new CommentLike(user, comment, true);
            comment.getCommentLikes().add(commentLike);
            commentLikeRepository.save(commentLike);
        }

        comment.updateCommentLikeCounts(); // 댓글의 좋아요 수 업데이트
        // commentRepository.save(comment); // 댓글 저장 (좋아요 상태 변경을 반영하기 위함)
    }

    // 아이디로 회원 찾기
    @Cacheable(value = "userId", key = "#userId")
    private User findByUserId(String userId) {
        if (userRepository.findByUserId(userId) == null) {
            throw new ErrorException("NOT FOUND ID");
        } else {
            return userRepository.findByUserId(userId)
                    .orElseThrow(() -> new ErrorException("CANNOT FIND USER"));
        }
    }

}
