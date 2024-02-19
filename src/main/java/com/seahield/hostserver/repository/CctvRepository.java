package com.seahield.hostserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.seahield.hostserver.domain.Cctv;

public interface CctvRepository extends JpaRepository<Cctv, String> {

}
