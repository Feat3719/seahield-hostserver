package com.seahield.hostserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.seahield.hostserver.domain.Company;

public interface CompanyRepository extends JpaRepository<Company, String> {
    boolean existsByCompanyRegistNum(String companyRegistNum);
}
