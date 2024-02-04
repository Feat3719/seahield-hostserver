package com.seahield.hostserver.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.seahield.hostserver.domain.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findByUserId(String userId);

    boolean findByUserContact(String userContact);

    Optional<User> findByUserEmail(String userEmail);

    boolean existsByUserId(String userId);

    boolean existsByUserEmail(String userEmail);

    boolean existsByUserContact(String userContact);

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.companyRegistNum IN :companyRegistNums")
    boolean existsByCompanyRegistNums(@Param("companyRegistNums") List<String> companyRegistNums);

    void deleteByUserId(String userId);
}
