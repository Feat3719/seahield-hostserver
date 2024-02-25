package com.seahield.hostserver.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.seahield.hostserver.domain.Announce;
import com.seahield.hostserver.domain.Company;
import com.seahield.hostserver.domain.Contract;
import com.seahield.hostserver.domain.ContractStatus;
import com.seahield.hostserver.domain.User;
import com.seahield.hostserver.domain.UserType;
import com.seahield.hostserver.dto.ContractDto.CreateContractRequest;
import com.seahield.hostserver.dto.ContractDto.ViewContractDetailsResponse;
import com.seahield.hostserver.dto.ContractDto.ViewContractListResponse;
import com.seahield.hostserver.exception.ErrorException;
import com.seahield.hostserver.repository.ContractRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContractService {
    private final ContractRepository contractRepository;
    private final AuthService authService;
    private final AnnounceService announceService;
    private final UserService userService;
    private final CompanyService companyService;

    // 수거 계약 신청 작성(CREATE)
    @Transactional
    public void createContract(CreateContractRequest request) {
        Company company = companyService.findCompanyByCompanyRegistNum(request.getCompanyRegistNum());
        Announce announce = announceService.findAnnounceByAnnounceId(request.getAnnounceId());
        Contract contract = new Contract(
                request.getContractAplDate(),
                request.getContractPrice(),
                ContractStatus.WAITING, company, announce);
        contractRepository.save(contract);
    }

    // 수거 계약 신청서 목록 조회(SELECT ALL)
    public List<ViewContractListResponse> viewContractList(String userId) {
        User user = userService.findByUserId(userId);
        UserType userType = user.getUserType();
        List<Contract> contracts;

        if (userType == UserType.ADMIN) {
            contracts = contractRepository.findAll();
        } else if (userType == UserType.BUSINESS) {
            String companyRegistNum = user.getCompany().getCompanyRegistNum();
            contracts = contractRepository.findByCompany_CompanyRegistNum(companyRegistNum)
                    .orElseThrow(() -> new ErrorException("NOT EXISTS CONTRACT"));
        } else {
            throw new ErrorException("NO PERMISSION");
        }

        return contracts.stream()
                .map(contract -> ViewContractListResponse.builder()
                        .contractId(contract.getContractId())
                        .contractAplDate(contract.getContractAplDate())
                        .contractStatus(contract.getContractStatus().getDescription())
                        .announceId(contract.getAnnounce().getAnnounceId())
                        .companyName(contract.getCompany().getCompanyName())
                        .build())
                .collect(Collectors.toList());
    }

    // 수거 계약 신청서 상세 조회(SELECT ONE)
    public ViewContractDetailsResponse viewContractDetails(String userId, Long contractId) {
        Contract contract = this.findContractByContractId(contractId);
        User user = userService.findByUserId(userId);
        UserType userType = user.getUserType();

        if (userType == UserType.ADMIN) {
            // ADMIN 유저는 모든 계약서에 접근할 수 있습니다.
            return buildContractDetailsResponse(contract);
        } else if (userType == UserType.BUSINESS) {
            // BUSINESS 유저는 자신의 회사가 작성한 계약서만 조회할 수 있습니다.
            if (!contract.getCompany().getCompanyRegistNum().equals(user.getCompany().getCompanyRegistNum())) {
                throw new ErrorException("NO PERMISSION TO VIEW THIS CONTRACT");
            }
            return buildContractDetailsResponse(contract);
        } else {
            throw new ErrorException("NO PERMISSION");
        }
    }

    private ViewContractDetailsResponse buildContractDetailsResponse(Contract contract) {
        // 공통된 Response 구성 로직
        return ViewContractDetailsResponse.builder()
                .contractId(contract.getContractId())
                .contractAplDate(contract.getContractAplDate())
                .contractPrice(contract.getContractPrice())
                .contractStatus(contract.getContractStatus().getDescription())
                .announceId(contract.getAnnounce().getAnnounceId())
                .announceName(contract.getAnnounce() != null ? contract.getAnnounce().getAnnounceName() : null)
                .announceContents(contract.getAnnounce() != null ? contract.getAnnounce().getAnnounceContents() : null)
                .announceCreatedDate(
                        contract.getAnnounce() != null ? contract.getAnnounce().getAnnounceCreatedDate() : null)
                .companyRegistNum(contract.getCompany().getCompanyRegistNum())
                .companyName(contract.getCompany().getCompanyName())
                .companyAddress(contract.getCompany().getCompanyAddress())
                .companyContact(contract.getCompany().getCompanyContact())
                .build();
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
                .orElseThrow(() -> new ErrorException("NOT EXISTS CONTRACT"));
    }

}
