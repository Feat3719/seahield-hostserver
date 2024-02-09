package com.seahield.hostserver.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.seahield.hostserver.domain.Contract;

public interface ContractRepository extends JpaRepository<Contract, Long> {

    Optional<Contract> findByContractId(Long contractId);
}
