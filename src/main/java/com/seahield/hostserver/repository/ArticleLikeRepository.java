package com.seahield.hostserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.seahield.hostserver.domain.Article;
import com.seahield.hostserver.domain.ArticleLike;
import com.seahield.hostserver.domain.User;
import java.util.Optional;

public interface ArticleLikeRepository extends JpaRepository<ArticleLike, Long> {
    Optional<ArticleLike> findByUserAndArticle(User user, Article article);
}
