package com.seahield.hostserver.service;

import java.util.List;

import org.springframework.stereotype.Service;
import com.seahield.hostserver.config.jwt.TokenProvider;
import com.seahield.hostserver.domain.QnaArticle;
import com.seahield.hostserver.domain.QnaComment;
import com.seahield.hostserver.domain.User;
import com.seahield.hostserver.dto.QnaCommentDto.CreateCommentRequest;
import com.seahield.hostserver.dto.QnaCommentDto.UpdateCommentRequest;
import com.seahield.hostserver.exception.ErrorException;
import com.seahield.hostserver.repository.QnaCommentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QnaCommentService {

    private final TokenProvider tokenProvider;
    private final AuthService authService;
    private final QnaArticleService qnaArticleService;
    private final QnaCommentRepository qnaCommentRepository;

    // 댓글 생성(CREATE)
    @Transactional
    public QnaComment addComment(String accessToken, CreateCommentRequest request) {
        String userId = tokenProvider.getUserId(accessToken);
        User user = authService.findByUserId(userId);
        QnaArticle article = qnaArticleService.findQnaArticleByQnaArticleId(request.getQnaArticleId());
        return qnaCommentRepository.save(request.toEntity(user, article));
    }

    // 댓글 수정(UPDATE)
    public void updateComments(long id, UpdateCommentRequest request) {
        QnaComment comment = this.findQnaCommentByQnaComment(id);
        comment.update(request.getQnaCommentContents());
    }

    private QnaComment findQnaCommentByQnaComment(Long qnaCommentId) {
        return qnaCommentRepository.findByQnaCommentId(qnaCommentId)
                .orElseThrow(() -> new ErrorException("NOT EXISTS COMMENT"));
    }

}
