package com.seahield.hostserver.service;

import org.springframework.stereotype.Service;

import com.seahield.hostserver.domain.Company;
import com.seahield.hostserver.dto.CompanyDto.CreateCompanyInfoRequest;
import com.seahield.hostserver.exception.ErrorException;
import com.seahield.hostserver.repository.CompanyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    // 법인 정보 불러오기(최초 신청시 입력되어 있는 기본 정보)

    // 법인 정보 생성
    public void createCompany(CreateCompanyInfoRequest request) {
        if (this.findCompanyByCompanyContact(request.getCompanyContact())) {
            Company company = new Company(request.getCompanyRegistNum(), request.getCompnayName(),
                    request.getCompanyAddress(), request.getCompanyContact());
            companyRepository.save(company);
        } else {
            throw new ErrorException("ALREADY EXISTS COMPANY_CONTACT");
        }

    }

    // 법인 중복검사 - 법인 연락처(이미 존재하면 false, 생성가능하면 true)
    private boolean findCompanyByCompanyContact(String companyContact) {
        return !companyRepository.existsByCompanyContact(companyContact);
    }

}
