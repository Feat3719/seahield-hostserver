package com.seahield.hostserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.seahield.hostserver.domain.QnaBoard;

@Repository
public interface QnaBoardRepository extends JpaRepository<QnaBoard, Long> {

}
