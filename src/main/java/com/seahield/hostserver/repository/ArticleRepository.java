package com.seahield.hostserver.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.seahield.hostserver.domain.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    Optional<Article> findByArticleId(Long articleId);

    void deleteByArticleId(Long articleId);
}
