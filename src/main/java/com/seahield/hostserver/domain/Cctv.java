package com.seahield.hostserver.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "CCTV")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cctv {

    @Id
    @Column(name = "cctv_id")
    private String cctvId;

    @Column(name = "cctv_address")
    private String cctvAddress;

}
