package com.seahield.hostserver.service;

import org.springframework.stereotype.Service;

import com.seahield.hostserver.domain.QnaBoard;
import com.seahield.hostserver.dto.QnaBoardDto.CreateArticleRequest;
import com.seahield.hostserver.exception.ErrorException;
import com.seahield.hostserver.repository.QnaBoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QnaBoardService {

    private final QnaBoardRepository qnaBoardRepository;
    private final AuthService authService;

    // 게시물 생성(CREATE)
    public QnaBoard addArticle(CreateArticleRequest request) {
        if (!authService.checkUserId(request.getWriter().getUserId())) {
            return qnaBoardRepository.save(request.toEntity());
        } else {
            throw new ErrorException("ERROR : SIGN IN FIRST");
        }
    }

    // 카테고리 조회(SELECT ALL)

}
