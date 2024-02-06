package com.seahield.hostserver.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleLikeRepository articleLikeRepository;
    private final AuthService authService;
    private final TokenProvider tokenProvider;

    // 게시글 생성(CREATE)
    @Transactional
    public Article addArticle(String accessToken, CreateArticleRequest request) {
        String userId = tokenProvider.getUserId(accessToken);
        User user = authService.findByUserId(userId);
        return articleRepository.save(request.toEntity(user));
    }

    // 게시글 목록 조회
    public List<ViewAllArticlesResponse> viewAllArticlesByCtgr(String articleCtgr, int page) {
        int pageSize = 10;
        Pageable pageable = PageRequest.of(page - 1, pageSize);

        // Fetch Join과 DTO를 사용하여 쿼리 최적화
        Page<ArticleProjection> articlePage = articleRepository.findAllProjectedByCtgr(articleCtgr, pageable);
        List<ArticleProjection> articleProjections = articlePage.getContent();

        // 변환 로직 (ArticleProjection -> ViewAllArticlesResponse)
        return articleProjections.stream()
                .map(projection -> new ViewAllArticlesResponse(
                        projection.getArticleId(),
                        projection.getArticleTitle(),
                        projection.getArticleCreatedDate(),
                        projection.getArticleWriterUserId(),
                        projection.getArticleViewCount(),
                        projection.getArticleLikeCount() // 좋아요 수
                ))
                .collect(Collectors.toList());
    }

    // 게시글 상세 조회
    @Transactional
    public ViewArticleResponse viewById(Long articleId) {
        Article article = articleRepository.findArticleWithCommentsAndLikesById(articleId)
                .orElseThrow(() -> new ErrorException("Article not found for id: " + articleId));

        List<ViewCommentResponse> commentResponses = convertToCommentResponseList(article.getComments());
        this.incrementArticleViewCount(articleId);
        return new ViewArticleResponse(
                article.getArticleId(),
                article.getArticleCtgr(),
                article.getArticleTitle(),
                article.getArticleContents(),
                article.getArticleCreatedDate(),
                article.getArticleUpdatedDate(),
                article.getArticleWriter().getUserId(),
                article.getArticleViewCounts(),
                article.getArticleLikeCount(), // Article 엔티티 내 좋아요 수 계산 메소드
                commentResponses // 변환된 댓글 정보 리스트
        );
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

    // 게시물 좋아요 토글 메소드
    @Transactional
    public void toggleLike(String accessToken, Long articleId) {
        String userId = tokenProvider.getUserId(accessToken);
        User user = authService.findByUserId(userId);
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
    @Cacheable(value = "articleCounts", key = "#articleId")
    public Long findArticleViewCountsByArticleId(Long articleId) {
        return this.findArticleByArticleId(articleId).getArticleViewCounts();
    }

    // 게시물 조회수 증가 메소드
    @Transactional
    @CachePut(value = "articleCounts", key = "#articleId")
    private Long incrementArticleViewCount(Long articleId) {
        Article article = this.findArticleByArticleId(articleId);
        article.setArticleViewCounts(article.getArticleViewCounts() + 1);
        articleRepository.save(article);
        return article.getArticleViewCounts();
    }

}
