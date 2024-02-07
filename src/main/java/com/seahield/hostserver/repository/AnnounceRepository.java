package com.seahield.hostserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.seahield.hostserver.domain.Announce;

public interface AnnounceRepository extends JpaRepository<Announce, String> {

}
