package com.seahield.hostserver.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "QNA_BOARD")
@Entity
@Getter
@Builder
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QnaBoard {

    @Id
    @GeneratedValue
    @Column(name = "qna_board_id", nullable = false)
    private Long qnaBoardId;

    @Column(name = "qna_board_ctgr", nullable = false)
    private String qnaBoardCtgr;

    @Column(name = "qna_board_title", nullable = false)
    private String qnaBoardTitle;

    @Column(name = "qna_board_contents", nullable = false)
    private String qnaBoardContents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qna_board_writer")
    private User qnaBoardWriter;

    @CreatedBy
    @Column(name = "qna_board_created_date", nullable = false)
    private LocalDateTime qnaBoardCreatedDate;

    @LastModifiedDate
    @Column(name = "qna_board_updated_date")
    private LocalDateTime qnaBoardUpdatedDate;

    @Column(name = "qna_board_view_counts")
    private Long qnaBoardViewCounts;

}
