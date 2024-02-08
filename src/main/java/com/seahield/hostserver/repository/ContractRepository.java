package com.seahield.hostserver.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.seahield.hostserver.domain.Contract;

public interface ContractRepository extends JpaRepository<Contract, Long> {
}
