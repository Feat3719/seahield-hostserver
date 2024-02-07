package com.seahield.hostserver.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Table(name = "CONTRACT")
@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Contract {

    @Id
    @Column(name = "contract_id", nullable = false) // 계약 신청서 번호
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long contractId;

    @CreatedDate
    @Column(name = "contract_apl_date", nullable = false) // 계약 신청 일자
    private LocalDate contractAplDate;

    @Column(name = "contract_price", nullable = false) // 계약 입찰 금액
    private Long contractPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_regist_num", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "announce_id", nullable = false)
    private Announce announce;

}
