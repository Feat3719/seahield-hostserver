package com.seahield.hostserver.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.seahield.hostserver.config.jwt.TokenProvider;
import com.seahield.hostserver.domain.Announce;
import com.seahield.hostserver.dto.AnnounceDto.CreateAnnounceRequest;
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
	private final TokenProvider tokenProvider;
	private final AuthService authService;

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
				.announceCreatedDate(announce.getAnnounceCreatedDate())
				.biddingStartDate(announce.getBiddingStartDate())
				.biddingEndDate(announce.getBiddingEndDate())
				.build();
	}

	// 공고 생성
	public void CreateAnnounce(String accessToken, CreateAnnounceRequest createAnnounceRequest) {
		if (authService.verifyAdmin(tokenProvider.getUserId(accessToken))) {
			Announce announce = new Announce(
					createAnnounceRequest.getAnnounceId(),
					createAnnounceRequest.getAnnounceName(),
					createAnnounceRequest.getAnnounceContents(),
					createAnnounceRequest.getBiddingStartDate(),
					createAnnounceRequest.getBiddingEndDate());
			announceRepository.save(announce);
		} else {
			throw new ErrorException("NO PERMISSION");
		}
	}

	// 공고 삭제
	@Transactional
	public void DeleteAnnnounce(String accessToken, String announceId) {
		if (authService.verifyAdmin(tokenProvider.getUserId(accessToken))) {
			announceRepository.deleteByAnnounceId(announceId);
		} else {
			throw new ErrorException("NO PERMISSION");
		}
	}

	// 공고 번호로 공고 찾기
	public Announce findAnnounceByAnnounceId(String announceId) {
		return announceRepository.findByAnnounceId(announceId)
				.orElseThrow(() -> new ErrorException("NOT EXISTS ANNOUNCEMENT"));
	}

}
