package com.seahield.hostserver.service;

import org.springframework.stereotype.Service;

import com.seahield.hostserver.domain.Company;
import com.seahield.hostserver.domain.User;
import com.seahield.hostserver.dto.CompanyDto.CreateCompanyInfoRequest;
import com.seahield.hostserver.dto.CompanyDto.ViewCompanyDefaultInfoResponse;
import com.seahield.hostserver.exception.ErrorException;
import com.seahield.hostserver.repository.CompanyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final UserService userService;

    // 법인 정보 불러오기(최초 신청시 입력되어 있는 기본 정보)
    public ViewCompanyDefaultInfoResponse viewCompanyDefaultInfo(String userId) {
        User user = userService.findByUserId(userId);

        return ViewCompanyDefaultInfoResponse.builder()
                .companyRegistNum(user.getCompany().getCompanyRegistNum())
                .userNickname(user.getUserNickname())
                .build();
    }

    // 법인 정보 생성
    public void createCompany(CreateCompanyInfoRequest request) {
        if (this.findCompanyByCompanyContact(request.getCompanyContact())) {
            Company company = new Company(request.getCompanyRegistNum(), request.getCompanyName(),
                    request.getCompanyAddress(), request.getCompanyContact());
            companyRepository.save(company);
        } else {
            throw new ErrorException("ALREADY EXISTS COMPANY_CONTACT");
        }

    }

    // 계약 신청 시 최초 여부 검증 (최초면 true, 기존 계약 경험 있으면 false)
    public boolean isCompanyContractFirst(String userId) {
        User user = userService.findByUserId(userId);
        String companyContact = findCompanyByCompanyRegistNum(user.getCompany().getCompanyRegistNum())
                .getCompanyContact();

        if (companyContact != null) {
            return false;

        } else {
            return true;
        }

    }

    // 법인 중복검사 - 법인 연락처(이미 존재하면 false, 생성가능하면 true)
    private boolean findCompanyByCompanyContact(String companyContact) {
        return !companyRepository.existsByCompanyContact(companyContact);
    }

    // 사업자등록번호로 법인 찾기
    public Company findCompanyByCompanyRegistNum(String companyRegistNum) {
        return companyRepository.findByCompanyRegistNum(companyRegistNum)
                .orElseThrow(() -> new ErrorException("NO EXISTS COMPANY"));
    }

}
