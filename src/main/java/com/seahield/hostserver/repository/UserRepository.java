package com.seahield.hostserver.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.seahield.hostserver.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUserId(String userId);

    boolean findByUserContact(String userContact);

    Optional<User> findByUserEmail(String userEmail);

    boolean existsByUserId(String userId);

    boolean existsByUserEmail(String userEmail);

    boolean existsByUserContact(String userContact);

    boolean existsByCompany_CompanyRegistNum(String companyRegistNum);

    void deleteByUserId(String userId);
}
