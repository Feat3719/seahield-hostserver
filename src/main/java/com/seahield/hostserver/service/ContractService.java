package com.seahield.hostserver.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.seahield.hostserver.domain.Announce;
import com.seahield.hostserver.domain.Company;
import com.seahield.hostserver.domain.Contract;
import com.seahield.hostserver.domain.ContractStatus;
import com.seahield.hostserver.domain.UserType;
import com.seahield.hostserver.dto.ContractDto.CreateContractRequest;
import com.seahield.hostserver.dto.ContractDto.ViewContractDetailsResponse;
import com.seahield.hostserver.dto.ContractDto.ViewContractListResponse;
import com.seahield.hostserver.exception.ErrorException;
import com.seahield.hostserver.repository.AnnounceRepository;
import com.seahield.hostserver.repository.ContractRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContractService {
    private final ContractRepository contractRepository;
    private final AnnounceRepository announceRepository;
    private final AuthService authService;
    private final UserService userService;
    private final CompanyService companyService;

    // 수거 계약 신청 작성(CREATE)
    @Transactional
    public void createContract(CreateContractRequest request) {
        Company company = companyService.findCompanyByCompanyRegistNum(request.getCompanyRegistNum());
        Announce announce = this.findAnnounceByAnnounceId(request.getAnnounceId());
        Contract contract = new Contract(
                request.getContractAplDate(),
                request.getContractPrice(),
                ContractStatus.WAITING, company, announce);
        contractRepository.save(contract);
    }

    // 수거 계약 신청서 목록 조회(SELECT ALL)
    public List<ViewContractListResponse> viewContractList(String userId) {
        if (userService.findUserType(userId) == UserType.ADMIN
                || userService.findUserType(userId) == UserType.BUSINESS) {
            List<Contract> contracts = contractRepository.findAll();
            return contracts.stream()
                    .map(contract -> ViewContractListResponse.builder()
                            .contractId(contract.getContractId())
                            .contractAplDate(contract.getContractAplDate())
                            .contractStatus(contract.getContractStatus().getDescription())
                            .announceId(contract.getAnnounce().getAnnounceId())
                            .companyName(contract.getCompany().getCompanyName())
                            .build())
                    .collect(Collectors.toList());

        } else
            throw new ErrorException("NO PERMISSION");
    }

    // 수거 계약 신청서 상세 조회(SELECT ONE)
    public ViewContractDetailsResponse viewContractDetails(String userId, Long contractId) {
        Contract contract = this.findContractByContractId(contractId);
        if (userService.findUserType(userId) == UserType.ADMIN) {
            return ViewContractDetailsResponse.builder()
                    .contractId(contract.getContractId())
                    .contractAplDate(contract.getContractAplDate())
                    .contractPrice(contract.getContractPrice())
                    .contractStatus(contract.getContractStatus().getDescription())
                    .announceId(contract.getAnnounce().getAnnounceId())
                    .companyRegistNum(contract.getCompany().getCompanyRegistNum())
                    .companyName(contract.getCompany().getCompanyName())
                    .companyAddress(contract.getCompany().getCompanyAddress())
                    .companyContact(contract.getCompany().getCompanyContact())
                    .build();

        } else if (userService.findUserType(userId) == UserType.BUSINESS) {
            return ViewContractDetailsResponse.builder()
                    .contractId(contract.getContractId())
                    .contractAplDate(contract.getContractAplDate())
                    .contractPrice(contract.getContractPrice())
                    .contractStatus(contract.getContractStatus().getDescription())
                    .announceId(contract.getAnnounce().getAnnounceId())
                    .announceName(contract.getAnnounce().getAnnounceName())
                    .announceContents(contract.getAnnounce().getAnnounceContents())
                    .announceCreatedDate(contract.getAnnounce().getAnnounceCreatedDate())
                    .companyRegistNum(contract.getCompany().getCompanyRegistNum())
                    .build();
        } else
            throw new ErrorException("NO PERMISSION");
    }

    // 수거 계약 승인 토글
    @Transactional
    public void updateContractStatusApprove(String userId, Long contractId) {
        if (authService.verifyAdmin(userId)) {
            Contract contract = findContractByContractId(contractId);
            contract.setContractStatus(ContractStatus.APPROVED);
            contractRepository.save(contract);
        } else {
            throw new ErrorException("NO PERMISSION");
        }
    }

    // 수거 계약 거절 토글
    @Transactional
    public void updateContractStatusReject(String userId, Long contractId) {
        if (authService.verifyAdmin(userId)) {
            Contract contract = findContractByContractId(contractId);
            contract.setContractStatus(ContractStatus.REJECTED);
            contractRepository.save(contract);
        } else {
            throw new ErrorException("NO PERMISSION");
        }
    }

    // 계약 번호로 계약 찾기
    private Contract findContractByContractId(Long contractId) {
        return contractRepository.findByContractId(contractId)
                .orElseThrow(() -> new ErrorException("NO EXISTS CONTRACT"));
    }

    // 공고 번호로 공고 찾기
    private Announce findAnnounceByAnnounceId(String announceId) {
        return announceRepository.findByAnnounceId(announceId);
    }
}
