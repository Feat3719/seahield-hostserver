package com.seahield.hostserver.service;

import org.springframework.stereotype.Service;
import com.seahield.hostserver.config.jwt.TokenProvider;
import com.seahield.hostserver.domain.Article;
import com.seahield.hostserver.domain.Comment;
import com.seahield.hostserver.domain.User;
import com.seahield.hostserver.dto.CommentDto.CreateCommentRequest;
import com.seahield.hostserver.dto.CommentDto.UpdateCommentRequest;
import com.seahield.hostserver.exception.ErrorException;
import com.seahield.hostserver.repository.CommentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardCommentService {

    private final TokenProvider tokenProvider;
    private final AuthService authService;
    private final BoardArticleService qnaArticleService;
    private final CommentRepository qnaCommentRepository;

    // 댓글 생성(CREATE)
    @Transactional
    public Comment addComment(String accessToken, CreateCommentRequest request) {
        String userId = tokenProvider.getUserId(accessToken);
        User user = authService.findByUserId(userId);
        Article article = qnaArticleService.findArticleByArticleId(request.getArticleId());
        return qnaCommentRepository.save(request.toEntity(user, article));
    }

    // 댓글 수정(UPDATE)
    @Transactional
    public void updateComment(Long id, UpdateCommentRequest request) {
        Comment comment = this.findCommentByCommentId(id);
        comment.update(request.getCommentContents());
    }

    // 댓글 삭제(DELETE)
    @Transactional
    public void deleteComment(Long id, UpdateCommentRequest request) {
        Comment comment = this.findCommentByCommentId(id);
        qnaCommentRepository.deleteByCommentId(comment.getCommentId());
    }

    // 댓글 ID 로 댓글 찾기
    private Comment findCommentByCommentId(Long qnaCommentId) {
        return qnaCommentRepository.findByCommentId(qnaCommentId)
                .orElseThrow(() -> new ErrorException("NOT FOUND COMMENT ID : " + qnaCommentId));
    }

    //

}
