package com.seahield.hostserver.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.ArrayList;

@Table(name = "ANNOUNCE")
@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Announce {

    @Id
    @Column(name = "announce_id", nullable = false) // 공고 번호
    private String announceId;

    @Column(name = "announce_name", nullable = false) // 공고 명
    private String announceName;

    @Column(name = "announce_contents", nullable = false) // 공고 내용
    private String announceContents;

    @CreatedDate
    @Column(name = "announce_created_date") // 공고 생성 날짜
    private LocalDate announceCreatedDate;

    @OneToMany(mappedBy = "announce")
    private List<Contract> contracts = new ArrayList<>();

}
