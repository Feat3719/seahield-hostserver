package com.seahield.hostserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.seahield.hostserver.domain.CommentLike;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

}
