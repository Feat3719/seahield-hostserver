package com.seahield.hostserver.service;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.seahield.hostserver.domain.Announce;
import com.seahield.hostserver.domain.Company;
import com.seahield.hostserver.domain.Contract;
import com.seahield.hostserver.domain.User;
import com.seahield.hostserver.dto.ContractDto.CreateContractRequest;
import com.seahield.hostserver.dto.ContractDto.ViewContractListResponse;
import com.seahield.hostserver.exception.ErrorException;
import com.seahield.hostserver.repository.AnnounceRepository;
import com.seahield.hostserver.repository.CompanyRepository;
import com.seahield.hostserver.repository.ContractRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContractService {
    private final ContractRepository contractRepository;
    private final AnnounceRepository announceRepository;
    private final CompanyRepository companyRepository;
    private final AuthService authService;

    // 수거 계약 신청 작성(CREATE)
    @Transactional
    public void createContract(CreateContractRequest request) {
        Company company = new Company(
                request.getCompanyRegistNum(),
                request.getCompanyName(),
                request.getCompanyAddress(),
                request.getCompanyContact());
        Announce annoucne = this.findAnnounceByAnnounceId(request.getAnnounceId());
        Contract contract = new Contract(
                request.getContractAplDate(),
                request.getContractPrice(),
                false, company, annoucne);
        companyRepository.save(company);
        contractRepository.save(contract);
    }

    // 수거 계약 신청서 목록 조회(SELECT ALL)
    public List<ViewContractListResponse> getContractList(String userId) {
        User user = authService.findByUserId(userId);
        if (user.getUserType().getDescription() == "관리자") {
            List<Contract> contracts = contractRepository.findAll();
            return contracts.stream()
                    .map(contract -> ViewContractListResponse.builder()
                            .contractId(contract.getContractId())
                            .contractAplDate(contract.getContractAplDate())
                            .announceId(contract.getAnnounce().getAnnounceId())
                            .companyName(contract.getCompany().getCompanyName())
                            .build())
                    .collect(Collectors.toList());

        } else {
            throw new ErrorException("NO PERMISSION");
        }
    }

    // 공고 번호로 공고 찾기
    private Announce findAnnounceByAnnounceId(String announceId) {
        return announceRepository.findByAnnounceId(announceId);
    }
}
