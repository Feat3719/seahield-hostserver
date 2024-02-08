package com.seahield.hostserver.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.io.Serializable;
import java.util.ArrayList;

@Table(name = "COMPANY")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "company_regist_num", nullable = false) // 사업자 등록 번호
    private String companyRegistNum;

    @Column(name = "company_name") // 법인 이름
    private String companyName;

    @Column(name = "company_address") // 법인 주소
    private String companyAddress;

    @Column(name = "company_contact") // 법인 전화번호
    private String companyContract;

    @OneToMany(mappedBy = "company")
    private List<Contract> contracts = new ArrayList<>();

    // @OneToOne(mappedBy = "company")
    // private User user;

    // 회원가입 시 생성자
    public Company(String companyRegistNum) {
        this.companyRegistNum = companyRegistNum;
    }

    // 수거 계약 신청시 생성자
    public Company(
            String companyRegistNum,
            String companyName,
            String companyAddress,
            String companyContract) {
        this.companyRegistNum = companyRegistNum;
        this.companyName = companyName;
        this.companyAddress = companyAddress;
        this.companyContract = companyRegistNum;
    }

}
