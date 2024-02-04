package com.seahield.hostserver.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.seahield.hostserver.config.jwt.TokenProvider;
import com.seahield.hostserver.domain.QnaArticle;
import com.seahield.hostserver.domain.QnaComment;
import com.seahield.hostserver.domain.User;
import com.seahield.hostserver.dto.QnaArticleDto.CreateArticleRequest;
import com.seahield.hostserver.dto.QnaArticleDto.UpdateArticleRequest;
import com.seahield.hostserver.dto.QnaArticleDto.ViewAllArticlesResponse;
import com.seahield.hostserver.dto.QnaArticleDto.ViewArticleResponse;
import com.seahield.hostserver.dto.QnaCommentDto.ViewCommentResponse;
import com.seahield.hostserver.exception.ErrorException;
import com.seahield.hostserver.repository.QnaArticleRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QnaArticleService {

    private final QnaArticleRepository qnaArticleRepository;
    private final AuthService authService;
    private final TokenProvider tokenProvider;

    // 게시글 생성(CREATE)
    @Transactional
    public QnaArticle addArticle(String accessToken, CreateArticleRequest request) {
        String userId = tokenProvider.getUserId(accessToken);
        User user = authService.findByUserId(userId);
        return qnaArticleRepository.save(request.toEntity(user));
    }

    // 게시글 목록 조회(SELECT ALL)
    public List<ViewAllArticlesResponse> viewAllArticles(int page) {
        int pageSize = 10; // 한 페이지당 글 개수, 필요에 따라 변경 가능
        Pageable pageable = PageRequest.of(page - 1, pageSize); // 페이지는 0부터 시작하므로 1을 빼줌
        Page<QnaArticle> articlePage = qnaArticleRepository.findAll(pageable);
        List<QnaArticle> articles = articlePage.getContent();

        // 변환 로직 (Article -> ViewAllArticlesResponse)
        List<ViewAllArticlesResponse> responses = articles.stream()
                .map(article -> new ViewAllArticlesResponse(
                        article.getQnaArticleId(),
                        article.getQnaArticleCtgr(),
                        article.getQnaArticleTitle(),
                        article.getQnaArticleCreatedDate(),
                        article.getQnaArticleWriter().getUserId(),
                        article.getQnaArticleViewCounts(),
                        article.getQnaArticleLikeCounts()))
                .collect(Collectors.toList());

        return responses;
    }

    // 게시글 상세 조회(SELECT ONE)
    public ViewArticleResponse viewById(long id) {
        QnaArticle article = findQnaArticleByQnaArticleId(id);

        List<ViewCommentResponse> commentResponses = convertToCommentResponseList(article.getComments());

        return new ViewArticleResponse(
                article.getQnaArticleId(),
                article.getQnaArticleCtgr(),
                article.getQnaArticleTitle(),
                article.getQnaArticleContents(),
                article.getQnaArticleCreatedDate(),
                article.getQnaArticleUpdatedDate(),
                article.getQnaArticleWriter().getUserId(),
                article.getQnaArticleViewCounts(),
                article.getQnaArticleLikeCounts(),
                commentResponses); // 댓글 리스트 추가
    }

    // 댓글 DTO 변환 로직
    private List<ViewCommentResponse> convertToCommentResponseList(List<QnaComment> comments) {
        return comments.stream()
                .map(comment -> new ViewCommentResponse(
                        comment.getQnaCommentId(),
                        comment.getQnaCommentContents(),
                        comment.getQnaCommentCreatedDate(),
                        comment.getQnaCommentUpdatedDate(),
                        comment.getQnaCommentLikeCounts(),
                        comment.getQnaCommentWriter()))
                .collect(Collectors.toList());
    }

    // 게시글 수정
    @Transactional
    public void updateArticle(long id, UpdateArticleRequest request) {
        QnaArticle article = findQnaArticleByQnaArticleId(id);
        article.update(request.getQnaArticleCtgr(), request.getQnaArticleTitle(), request.getQnaArticleContents());
    }

    // 게시글 삭제
    @Transactional
    public void delete(long id) {
        QnaArticle article = findQnaArticleByQnaArticleId(id); // 게시글이 존재하는지 확인
        qnaArticleRepository.delete(article);
    }

    // 게시글 ID 로 게시글 찾기
    public QnaArticle findQnaArticleByQnaArticleId(Long qnaArticleId) {
        return qnaArticleRepository.findByQnaArticleId(qnaArticleId)
                .orElseThrow(() -> new ErrorException("Article with id " + qnaArticleId + " was not found"));
    }

    // 게시글 추천 수 + 1 로직(캐싱)
    @Transactional
    @CachePut(value = "articles", key = "#id")
    public QnaArticle increaseLikeCount(Long qnaArticleId) {
        QnaArticle article = this.findQnaArticleByQnaArticleId(qnaArticleId);

        article.plusQnaArticleLikeCounts(article.getQnaArticleLikeCounts() + 1);
        return qnaArticleRepository.save(article);
    }

}
