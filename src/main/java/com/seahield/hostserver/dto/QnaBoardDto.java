package com.seahield.hostserver.dto;

import com.seahield.hostserver.domain.QnaBoard;
import com.seahield.hostserver.domain.User;

import lombok.Builder;
import lombok.Getter;

public class QnaBoardDto {

    // 게시글 작성 RequestDto
    @Getter
    @Builder
    public static class CreateArticleRequest {
        private String qnaBoardCtgr;
        private String qnaBoardTitle;
        private String qnaBoardContents;
        private User writer;

        public QnaBoard toEntity() {
            return QnaBoard.builder()
                    .qnaBoardCtgr(qnaBoardCtgr)
                    .qnaBoardTitle(qnaBoardTitle)
                    .qnaBoardContents(qnaBoardContents)
                    .qnaBoardWriter(writer)
                    .build();
        }

    }

    // 카테고리 조회 RequestDto
    // @Getter
    // public static class ViewQnaBoardRequest {
    // private String
    // }

}
