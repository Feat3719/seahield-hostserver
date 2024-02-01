package com.seahield.hostserver.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.seahield.hostserver.domain.RefreshToken;

import jakarta.transaction.Transactional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUserId(String userId);

    Optional<RefreshToken> findByRefreshToken(String refreshToken);

    @Transactional
    void deleteByUserId(String userId);
}
