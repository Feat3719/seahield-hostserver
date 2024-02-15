package com.seahield.hostserver.service;

import org.springframework.stereotype.Service;

import com.seahield.hostserver.domain.Announce;
import com.seahield.hostserver.dto.AnnounceDto.ViewAnnounceInApply;
import com.seahield.hostserver.dto.AnnounceDto.ViewAnnounceInCtgr;
import com.seahield.hostserver.exception.ErrorException;
import com.seahield.hostserver.repository.AnnounceRepository;

import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnnounceService {

        private final AnnounceRepository announceRepository;

        // 수거 신청 내역에서 공고 조회
        public List<ViewAnnounceInApply> getAnnounceInApply() {
                List<Announce> announces = announceRepository.findAll();
                return announces.stream()
                                .map(announce -> ViewAnnounceInApply.builder()
                                                .announceId(announce.getAnnounceId())
                                                .announceName(announce.getAnnounceName())
                                                .announceCreatedDate(announce.getAnnounceCreatedDate())
                                                .build())
                                .collect(Collectors.toList());
        }

        // 공고 게시판에서 공고 조회
        public ViewAnnounceInCtgr getAnnounceInCtgr(String announceId) {
                Announce announce = announceRepository.findByAnnounceId(announceId)
                                .orElseThrow(() -> new ErrorException("NOT FOUND ANNOUNCE"));
                return ViewAnnounceInCtgr.builder()
                                .announceId(announce.getAnnounceId())
                                .announceName(announce.getAnnounceName())
                                .announceContents(announce.getAnnounceContents())
                                .aanounceCreatedDate(announce.getAnnounceCreatedDate())
                                .biddingStartDate(announce.getBiddingStartDate())
                                .biddingEndDate(announce.getBiddingEndDate())
                                .build();
        }

}
