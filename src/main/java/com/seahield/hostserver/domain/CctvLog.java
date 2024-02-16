package com.seahield.hostserver.domain;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "CCTV_LOG", indexes = {
        @Index(name = "idx_cctv_log_id", columnList = "cctv_log_id", unique = true)
})
@Entity
@Getter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CctvLog {

    @Id
    @Column(name = "cctv_log_id")
    private Long cctvLogId;

    @Column(name = "cctv_id")
    private String cctvId;

    @Column(name = "detected_date")
    private LocalDateTime detectedDate;

    @Column(name = "object_count")
    private int objectCount;

    @Column(name = "risk_index")
    private int riskIndex;

    @Column(name = "PET_bottle_per")
    private float petBottlePer;

    @Column(name = "plastic_ETC_per")
    private float plasticEtcPer;

    @Column(name = "Metal_per")
    private float metalPer;

    @Column(name = "Glass_per")
    private float glassPer;

    @Column(name = "Net_per")
    private float netPer;

    @Column(name = "Rope_per")
    private float ropePer;

    @Column(name = "Plastic_Buoy_China_per")
    private float plasticBuoyChinaPer;

    @Column(name = "Plastic_Buoy_per")
    private float plasticBuoyPer;

    @Column(name = "Styrofoam_Piece_per")
    private float styrofoamPiecePer;

    @Column(name = "Styrofoam_Buoy_per")
    private float styrofoamBuoyPer;

    @Column(name = "Styrofoam_Box_per")
    private float styrofoamBoxPer;

    @Column(name = "PET_Bottle_cnt")
    private int petBottleCnt;

    @Column(name = "Plastic_ETC_cnt")
    private int plasticEtcCnt;

    @Column(name = "Metal_cnt")
    private int metalCnt;

    @Column(name = "Glass_cnt")
    private int glassCnt;

    @Column(name = "Net_cnt")
    private int netCnt;

    @Column(name = "Rope_cnt")
    private int ropeCnt;

    @Column(name = "Plastic_Buoy_China_cnt")
    private int plasticBuoyChinaCnt;

    @Column(name = "Plastic_Buoy_cnt")
    private int plasticBuoyCnt;

    @Column(name = "Styrofoam_Piece_cnt")
    private int styrofoamPieceCnt;

    @Column(name = "Styrofoam_Buoy_cnt")
    private int styrofoamBuoyCnt;

    @Column(name = "Styrofoam_Box_cnt")
    private int styrofoamBoxCnt;

}
