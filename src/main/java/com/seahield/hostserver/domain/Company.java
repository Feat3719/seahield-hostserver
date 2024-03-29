package com.seahield.hostserver.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.io.Serializable;
import java.util.ArrayList;

@Table(name = "COMPANY", indexes = {
        @Index(name = "idx_company_regist_num", columnList = "company_regist_num", unique = true)
})
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Company implements Serializable {

    private static final long serialVersionUID = 100L;

    @Id
    @Column(name = "company_regist_num", nullable = false) // 사업자 등록 번호
    private String companyRegistNum;

    @Column(name = "company_name") // 법인 이름
    private String companyName;

    @Column(name = "company_address") // 법인 주소
    private String companyAddress;

    @Column(name = "company_contact") // 법인 전화번호
    private String companyContact;

    @OneToMany(mappedBy = "company")
    private List<Contract> contracts = new ArrayList<>();

    // 회원가입 시 생성자
    public Company(String companyRegistNum) {
        this.companyRegistNum = companyRegistNum;
    }

    // 수거 계약 신청시 생성자
    public Company(
            String companyRegistNum,
            String companyName,
            String companyAddress,
            String companyContact) {
        this.companyRegistNum = companyRegistNum;
        this.companyName = companyName;
        this.companyAddress = companyAddress;
        this.companyContact = companyContact;
    }

}
