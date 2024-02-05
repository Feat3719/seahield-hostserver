package com.seahield.hostserver.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.seahield.hostserver.config.jwt.TokenProvider;
import com.seahield.hostserver.domain.Article;
import com.seahield.hostserver.domain.ArticleLike;
import com.seahield.hostserver.domain.Comment;
import com.seahield.hostserver.domain.User;
import com.seahield.hostserver.dto.ArticleDto.CreateArticleRequest;
import com.seahield.hostserver.dto.ArticleDto.UpdateArticleRequest;
import com.seahield.hostserver.dto.ArticleDto.ViewAllArticlesResponse;
import com.seahield.hostserver.dto.ArticleDto.ViewArticleResponse;
import com.seahield.hostserver.dto.CommentDto.ViewCommentResponse;
import com.seahield.hostserver.exception.ErrorException;
import com.seahield.hostserver.repository.ArticleLikeRepository;
import com.seahield.hostserver.repository.ArticleRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleLikeRepository articleLikeRepository;
    private final AuthService authService;
    private final TokenProvider tokenProvider;

    private RedisTemplate<String, Long> redisTemplate;

    // 게시글 생성(CREATE)
    @Transactional
    public Article addArticle(String accessToken, CreateArticleRequest request) {
        String userId = tokenProvider.getUserId(accessToken);
        User user = authService.findByUserId(userId);
        return articleRepository.save(request.toEntity(user));
    }

    // 게시글 목록 조회(SELECT ALL)
    public List<ViewAllArticlesResponse> viewAllArticles(int page) {
        int pageSize = 10; // 한 페이지당 글 개수, 필요에 따라 변경 가능
        Pageable pageable = PageRequest.of(page - 1, pageSize); // 페이지는 0부터 시작하므로 1을 빼줌
        Page<Article> articlePage = articleRepository.findAll(pageable);
        List<Article> articles = articlePage.getContent();

        // 변환 로직 (Article -> ViewAllArticlesResponse)
        List<ViewAllArticlesResponse> responses = articles.stream()
                .map(article -> new ViewAllArticlesResponse(
                        article.getArticleId(),
                        article.getArticleCtgr(),
                        article.getArticleTitle(),
                        article.getArticleCreatedDate(),
                        article.getArticleWriter().getUserId(),
                        article.getArticleViewCounts(),
                        article.getArticleLikeCounts()))
                .collect(Collectors.toList());

        return responses;
    }

    // 게시글 상세 조회(SELECT ONE)
    public ViewArticleResponse viewById(Long id) {
        Article article = findArticleByArticleId(id);

        List<ViewCommentResponse> commentResponses = convertToCommentResponseList(article.getComments());

        return new ViewArticleResponse(
                article.getArticleId(),
                article.getArticleCtgr(),
                article.getArticleTitle(),
                article.getArticleContents(),
                article.getArticleCreatedDate(),
                article.getArticleUpdatedDate(),
                article.getArticleWriter().getUserId(),
                article.getArticleViewCounts(),
                article.getArticleLikeCounts(),
                commentResponses); // 댓글 리스트 추가
    }

    // 댓글 DTO 변환 로직
    private List<ViewCommentResponse> convertToCommentResponseList(List<Comment> comments) {
        return comments.stream()
                .map(comment -> new ViewCommentResponse(
                        comment.getCommentId(),
                        comment.getCommentContents(),
                        comment.getCommentCreatedDate(),
                        comment.getCommentUpdatedDate(),
                        comment.getCommentLikeCounts(),
                        comment.getCommentWriter()))
                .collect(Collectors.toList());
    }

    // 게시글 수정
    @Transactional
    public void updateArticle(Long id, UpdateArticleRequest request) {
        Article article = findArticleByArticleId(id);
        article.update(request.getArticleCtgr(), request.getArticleTitle(), request.getArticleContents());
    }

    // 게시글 삭제
    @Transactional
    public void delete(Long id) {
        Article article = findArticleByArticleId(id); // 게시글이 존재하는지 확인
        articleRepository.delete(article);
    }

    // 게시글 ID 로 게시글 찾기
    public Article findArticleByArticleId(Long articleId) {
        return articleRepository.findByArticleId(articleId)
                .orElseThrow(() -> new ErrorException("NOT FOUND ARTICLE : " + articleId));
    }

    // // 게시글 추천 수 + 1 로직(캐싱)
    // @Transactional
    // @CachePut(value = "articles", key = "#id")
    // public Article increaseLikeCount(Long qnaArticleId) {
    // Article article = this.findQnaArticleByQnaArticleId(qnaArticleId);

    // article.plusQnaArticleLikeCounts(article.getArticleLikeCounts() + 1);
    // return qnaArticleRepository.save(article);
    // }

    // 사용자가 좋아요 버튼을 눌렀을 때 처리
    public void toggleLike(String accessToken, Long articleId) {
        String userId = tokenProvider.getUserId(accessToken);
        User user = authService.findByUserId(userId);
        Article article = this.findArticleByArticleId(articleId);

        ArticleLike articleLike = articleLikeRepository.findByUserAndArticle(user, article)
                .orElse(new ArticleLike(user, article, false));

        // 좋아요 상태 토글
        articleLike.setArticleLikeStatus(!articleLike.isArticleLikeStatus());
        articleLikeRepository.save(articleLike);

        // 좋아요 상태에 따라 게시글의 좋아요 수를 업데이트
        if (articleLike.isArticleLikeStatus()) {
            article.incrementLikeCount(); // 좋아요 개수 증가
        } else {
            article.decrementLikeCount(); // 좋아요 개수 감소
        }
        articleRepository.save(article); // 게시글 저장
    }

}
