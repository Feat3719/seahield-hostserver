package com.seahield.hostserver.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.seahield.hostserver.domain.QnaComment;

@Repository
public interface QnaCommentRepository extends JpaRepository<QnaComment, Long> {
    Optional<QnaComment> findByQnaCommentId(Long qnaCommentId);
}
