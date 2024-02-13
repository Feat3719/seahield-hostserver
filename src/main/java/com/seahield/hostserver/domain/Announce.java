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
import java.util.ArrayList;

@Table(name = "ANNOUNCE", indexes = {
        @Index(name = "idx_announce_id", columnList = "announce_id", unique = true) })
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Announce {

    @Id
    @Column(name = "announce_id", nullable = false) // 공고 번호
    private String announceId;

    @Column(name = "announce_name", nullable = false) // 공고 제목
    private String announceName;

    @Column(name = "announce_contents", nullable = false) // 공고 내용
    private String announceContents;

    @Column(name = "announce_created_date", nullable = false) // 공고 생성 날짜
    private String announceCreatedDate;

    @Column(name = "bidding_start_date", nullable = false) // 입찰 시작 일시
    private String biddingStartDate;

    @Column(name = "bidding_end_date", nullable = false) // 입찰 마감 일시
    private String biddingEndDate;

    @OneToMany(mappedBy = "announce")
    private List<Contract> contracts = new ArrayList<>();

}
