package com.seahield.hostserver.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.seahield.hostserver.config.jwt.TokenProvider;
import com.seahield.hostserver.domain.Article;
import com.seahield.hostserver.domain.ArticleLike;
import com.seahield.hostserver.domain.Comment;
import com.seahield.hostserver.domain.User;
import com.seahield.hostserver.domain.projection.ArticleProjection;
import com.seahield.hostserver.dto.ArticleDto.CreateArticleRequest;
import com.seahield.hostserver.dto.ArticleDto.UpdateArticleRequest;
import com.seahield.hostserver.dto.ArticleDto.ViewAllArticlesResponse;
import com.seahield.hostserver.dto.ArticleDto.ViewArticleResponse;
import com.seahield.hostserver.dto.CommentDto.ViewCommentResponse;
import com.seahield.hostserver.exception.ErrorException;
import com.seahield.hostserver.repository.ArticleLikeRepository;
import com.seahield.hostserver.repository.ArticleRepository;
import com.seahield.hostserver.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleLikeRepository articleLikeRepository;
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    // 게시글 생성(CREATE)
    @Transactional
    public Article addArticle(String accessToken, CreateArticleRequest request) {
        String userId = tokenProvider.getUserId(accessToken);
        User user = this.findByUserId(userId);
        return articleRepository.save(request.toEntity(user));
    }

    // 게시글 목록 조회
    // @Cacheable(value = "articlesByCategory", key = "#articleCtgr")
    @Transactional(readOnly = true)
    public List<ViewAllArticlesResponse> viewAllArticlesByCtgr(String articleCtgr) {

        // Fetch Join과 DTO를 사용하여 쿼리 최적화
        List<ArticleProjection> articleProjections = articleRepository.findAllProjectedByCtgr(articleCtgr)
                .orElseThrow(() -> new ErrorException("NOT EXISTS ARTICLES"));

        // 변환 로직 (ArticleProjection -> ViewAllArticlesResponse)
        return articleProjections.stream()
                .map(projection -> new ViewAllArticlesResponse(
                        projection.getArticleId(),
                        projection.getArticleTitle(),
                        projection.getArticleCreatedDate(),
                        projection.getArticleWriterUserId(),
                        projection.getArticleViewCounts(),
                        projection.getArticleLikeCount())) // 좋아요 수
                .collect(Collectors.toList());
    }

    // 게시글 상세 조회
    @Transactional(readOnly = true)
    public ViewArticleResponse viewById(Long articleId) {
        Article article = articleRepository.findByArticleId(articleId)
                .orElseThrow(() -> new ErrorException("Article not found for id: " + articleId));

        List<ViewCommentResponse> commentResponses = convertToCommentResponseList(article.getComments());
        this.incrementArticleViewCount(articleId);
        return ViewArticleResponse.builder()
                .articleId(article.getArticleId())
                .articleCtgr(article.getArticleCtgr())
                .articleTitle(article.getArticleTitle())
                .articleContents(article.getArticleContents())
                .articleCreatedDate(article.getArticleCreatedDate())
                .articleUpdateDate(article.getArticleUpdatedDate())
                .userId(article.getArticleWriter().getUserId())
                .articleViewCount(this.findArticleViewCountsByArticleId(articleId))
                .articleLikes(article.getArticleLikeCount())
                .comments(commentResponses)
                .build();
    }

    // 댓글 DTO 변환 로직
    private List<ViewCommentResponse> convertToCommentResponseList(List<Comment> comments) {
        return comments.stream()
                .map(comment -> new ViewCommentResponse(
                        comment.getCommentId(),
                        comment.getCommentContents(),
                        comment.getCommentCreatedDate(),
                        comment.getCommentUpdatedDate(),
                        comment.getLikeCount(), // Comment 엔티티 내 좋아요 수 계산 메소드
                        comment.getCommentWriter().getUserId()))
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
        Article article = this.findArticleByArticleId(id); // 게시글이 존재하는지 확인
        articleRepository.delete(article);
    }

    // 게시글 ID 로 게시글 찾기
    public Article findArticleByArticleId(Long articleId) {
        return articleRepository.findByArticleId(articleId)
                .orElseThrow(() -> new ErrorException("NOT FOUND ARTICLE : " + articleId));
    }

    // 유저 ID로 게시글 찾기
    public List<Article> findArticleByUserId(String userId) {
        User user = this.findByUserId(userId);
        return articleRepository.findByArticleWriter(user)
                .orElseThrow(() -> new ErrorException("NOT FOUND ARTICLE"));
    }

    // 유저가 좋아요한 게시글 찾기
    public List<Article> findArticleByUserLikesArticle(String userId) {
        User user = this.findByUserId(userId);
        List<ArticleLike> likes = articleLikeRepository.findByUser(user)
                .orElseThrow(() -> new ErrorException("NOT EXSISTS LIKE DATE"));
        return likes.stream().map(ArticleLike::getArticle).collect(Collectors.toList());
    }

    // 게시물 좋아요 토글 메소드
    @Transactional
    public void toggleLike(String accessToken, Long articleId) {
        String userId = tokenProvider.getUserId(accessToken);
        User user = this.findByUserId(userId);
        Article article = this.findArticleByArticleId(articleId);

        Optional<ArticleLike> articleLikeOpt = articleLikeRepository.findByUserAndArticle(user, article);

        if (articleLikeOpt.isPresent()) {
            // 좋아요가 이미 존재한다면, 해당 좋아요를 삭제합니다.
            ArticleLike articleLike = articleLikeOpt.get();
            article.getArticleLikes().remove(articleLike);
            articleLikeRepository.delete(articleLike);
        } else {
            // 좋아요가 존재하지 않는다면, 새로운 좋아요를 추가합니다.
            ArticleLike articleLike = new ArticleLike(user, article, true);
            article.getArticleLikes().add(articleLike);
            articleLikeRepository.save(articleLike);
        }

        article.updateArticleLikeCounts();
        articleRepository.save(article); // 게시글 저장 (좋아요 상태 변경을 반영하기 위함)
    }

    // 게시물 Id로 조회수 찾기
    public Long findArticleViewCountsByArticleId(Long articleId) {
        return this.findArticleByArticleId(articleId).getArticleViewCounts();
    }

    // 게시물 조회수 증가 메소드
    @Transactional
    private void incrementArticleViewCount(Long articleId) {
        articleRepository.incrementViewCount(articleId);
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
