package com.seahield.hostserver.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.seahield.hostserver.domain.QnaArticle;

@Repository
public interface QnaArticleRepository extends JpaRepository<QnaArticle, Long> {
    Optional<QnaArticle> findByQnaArticleId(long qnaArticleId);

    void deleteByQnaArticleId(long qnaArticleId);
}
