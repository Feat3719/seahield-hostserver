package com.seahield.hostserver.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.seahield.hostserver.domain.Comment;
import java.util.List;
import com.seahield.hostserver.domain.User;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findByCommentId(Long commentId);

    void deleteByCommentId(Long commentId);

    Optional<List<Comment>> findByCommentWriter(User commentWriter);
}
