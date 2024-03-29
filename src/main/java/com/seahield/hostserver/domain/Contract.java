package com.seahield.hostserver.domain;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "CONTRACT", indexes = {
        @Index(name = "index_contract_id", columnList = "contract_id", unique = true)
})
@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Contract {

    @Id
    @Column(name = "contract_id", nullable = false) // 계약 신청서 번호
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long contractId;

    @Column(name = "contract_apl_date", nullable = false) // 계약 신청 일자
    private String contractAplDate;

    @Column(name = "contract_price", nullable = false) // 계약 입찰 금액
    private Long contractPrice;

    @Column(name = "contract_status", nullable = false) // 계약 승인 여부
    @Enumerated(EnumType.STRING)
    private ContractStatus contractStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_regist_num", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "announce_id", nullable = false)
    private Announce announce;

    // 계약서 작성 생성자
    public Contract(
            String contractAplDate,
            Long contractPrice,
            ContractStatus contractStatus,
            Company company,
            Announce announce) {
        this.contractAplDate = contractAplDate;
        this.contractPrice = contractPrice;
        this.contractStatus = contractStatus;
        this.company = company;
        this.announce = announce;
    }

    // 계약서 상태 토글
    public void setContractStatus(ContractStatus contractStatus) {
        this.contractStatus = contractStatus;
    }

}
