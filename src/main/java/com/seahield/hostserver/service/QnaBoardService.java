package com.seahield.hostserver.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.seahield.hostserver.config.jwt.TokenProvider;
import com.seahield.hostserver.domain.QnaArticle;
import com.seahield.hostserver.domain.User;
import com.seahield.hostserver.dto.QnaArticleDto.CreateArticleRequest;
import com.seahield.hostserver.dto.QnaArticleDto.UpdateArticleRequest;
import com.seahield.hostserver.dto.QnaArticleDto.ViewAllArticlesResponse;
import com.seahield.hostserver.dto.QnaArticleDto.ViewArticleResponse;
import com.seahield.hostserver.exception.ErrorException;
import com.seahield.hostserver.repository.QnaArticleRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QnaBoardService {

    private final QnaArticleRepository qnaBoardRepository;
    private final AuthService authService;
    private final TokenProvider tokenProvider;

    // 게시글 생성(CREATE)
    @Transactional
    public QnaArticle addArticle(String accessToken, CreateArticleRequest request) {
        String userId = tokenProvider.getUserId(accessToken);
        User user = authService.findByUserId(userId);
        return qnaBoardRepository.save(request.toEntity(user));
    }

    // 게시글 목록 조회(SELECT ALL)
    public List<ViewAllArticlesResponse> viewAllArticles(int page) {
        int pageSize = 10; // 한 페이지당 글 개수, 필요에 따라 변경 가능
        Pageable pageable = PageRequest.of(page - 1, pageSize); // 페이지는 0부터 시작하므로 1을 빼줌
        Page<QnaArticle> articlePage = qnaBoardRepository.findAll(pageable);
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
        QnaArticle article = qnaBoardRepository.findByQnaArticleId(id)
                .orElseThrow(() -> new ErrorException("Article with id " + id + " was not found"));

        // 엔티티를 DTO로 변환합니다.
        return new ViewArticleResponse(
                article.getQnaArticleId(),
                article.getQnaArticleCtgr(),
                article.getQnaArticleTitle(),
                article.getQnaArticleContents(),
                article.getQnaArticleCreatedDate(),
                article.getQnaArticleUpdatedDate(),
                article.getQnaArticleWriter().getUserId(),
                article.getQnaArticleViewCounts(),
                article.getQnaArticleLikeCounts());
    }

    // 게시글 수정
    @Transactional
    public void updateArticle(long id, UpdateArticleRequest request) {
        QnaArticle article = qnaBoardRepository.findByQnaArticleId(id)
                .orElseThrow(() -> new ErrorException("NOT FOUND"));
        article.update(request.getQnaArticleCtgr(), request.getQnaArticleTitle(), request.getQnaArticleContents());

    }

    // 게시글 삭제
    @Transactional
    public boolean delete(long id) {
        if (qnaBoardRepository.findByQnaArticleId(id) == null) {
            throw new ErrorException("INCORRECT ARTICLE ID");
        } else {
            qnaBoardRepository.deleteByQnaArticleId(id);
            return true;
        }
    }

}
