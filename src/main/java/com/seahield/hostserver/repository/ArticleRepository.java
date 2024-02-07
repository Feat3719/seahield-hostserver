package com.seahield.hostserver.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.seahield.hostserver.domain.Article;
import com.seahield.hostserver.domain.projection.ArticleProjection;
import com.seahield.hostserver.domain.User;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
        Optional<Article> findByArticleId(Long articleId);

        void deleteByArticleId(Long articleId);

        // 게시글 조회수 업데이트
        // void updateArticleViewCount(Long articleId, Long articleViewCounts);

        // ArticleViewCountDto findArticleViewCountById(Long articleId);
        @Query("SELECT a.id as articleId, a.articleCtgr as articleCtgr, a.articleTitle as articleTitle, a.articleCreatedDate as articleCreatedDate, a.articleWriter.userId as articleWriterUserId, a.articleViewCounts as articleViewCounts "
                        +
                        "FROM Article a LEFT JOIN a.articleLikes al " +
                        "GROUP BY a.id")
        Page<ArticleProjection> findAllProjectedBy(Pageable pageable);

        @Query("SELECT a FROM Article a LEFT JOIN FETCH a.comments c LEFT JOIN FETCH c.commentLikes cl WHERE a.articleId = :articleId")
        Optional<Article> findArticleWithCommentsAndLikesById(@Param("articleId") Long articleId);

        @Query("SELECT a.id as articleId, a.articleCtgr as articleCtgr, a.articleTitle as articleTitle, a.articleCreatedDate as articleCreatedDate, a.articleWriter.userId as articleWriterUserId, a.articleViewCounts as articleViewCounts, count(al) as articleLikeCount "
                        +
                        "FROM Article a LEFT JOIN a.articleLikes al " +
                        "WHERE a.articleCtgr = :articleCtgr " +
                        "GROUP BY a.id")
        List<ArticleProjection> findAllProjectedByCtgr(String articleCtgr);

        Optional<List<Article>> findByArticleWriter(User articleWriter);

        // Optional<List<Article>> findAllByArticleLikes(Optional<Set<ArticleLike>>
        // articleLikes);
        // @Query("SELECT a FROM Article a JOIN a.likes l WHERE l.user = :user")
        // List<Article> findArticlesLikedByUser(@Param("user") User user);

}