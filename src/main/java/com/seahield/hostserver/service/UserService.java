package com.seahield.hostserver.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.seahield.hostserver.config.jwt.TokenProvider;
import com.seahield.hostserver.domain.Article;
import com.seahield.hostserver.domain.Comment;
import com.seahield.hostserver.domain.Company;
import com.seahield.hostserver.domain.User;
import com.seahield.hostserver.dto.ArticleDto.ViewMyArticleResponse;
import com.seahield.hostserver.dto.CommentDto.ViewMyCommentsResponse;
import com.seahield.hostserver.dto.UserDto.EditUserInfoRequest;
import com.seahield.hostserver.dto.UserDto.ViewUserInfoResponse;
import com.seahield.hostserver.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

        private final TokenProvider tokenProvider;
        private final AuthService authService;
        private final BoardArticleService boardArticleService;
        private final BoardCommentService boardCommentService;
        private final BCryptPasswordEncoder bCryptPasswordEncoder;
        private final UserRepository userRepository;

        // 회원 정보 조회
        @Transactional(readOnly = true)
        public ViewUserInfoResponse getUserInfo(String accessToken) {
                String userId = tokenProvider.getUserId(accessToken);
                User user = authService.findByUserId(userId);
                Company company = user.getCompany();
                if (company != null) {
                        String companyRegistNum = company.getCompanyRegistNum();
                        return ViewUserInfoResponse.builder()
                                        .userId(user.getUserId())
                                        .userNickName(user.getUserNickName())
                                        .userEmail(user.getUserEmail())
                                        .userContact(user.getUserContact())
                                        .userAddress(user.getUserAddress())
                                        .userType(user.getUserType().getDescription())
                                        .companyRegistNum(companyRegistNum)
                                        .userJoinedYmd(user.getUserJoinedYmd())
                                        .build();
                } else {
                        return ViewUserInfoResponse.builder()
                                        .userId(user.getUserId())
                                        .userNickName(user.getUserNickName())
                                        .userEmail(user.getUserEmail())
                                        .userContact(user.getUserContact())
                                        .userAddress(user.getUserAddress())
                                        .userType(user.getUserType().getDescription())
                                        .userJoinedYmd(user.getUserJoinedYmd())
                                        .build();
                }

        }

        // 회원 정보 수정
        @Transactional
        public void editUserInfo(String accessToken, EditUserInfoRequest request) {
                String userId = tokenProvider.getUserId(accessToken);
                User user = authService.findByUserId(userId);
                user.setUserInfo(bCryptPasswordEncoder.encode(request.getUserPwd()), request.getUserNickName(),
                                request.getUserAddress());
                userRepository.save(user);
        }

        // 내가 작성한 글 조회
        @Transactional(readOnly = true)
        public List<ViewMyArticleResponse> getUserArticles(String userId) {
                // ArticleRepository에서 사용자 ID를 기반으로 글을 조회하는 메소드를 호출
                List<Article> articles = boardArticleService.findArticleByUserId(userId);
                return articles.stream()
                                .map(article -> ViewMyArticleResponse.builder()
                                                .articleId(article.getArticleId())
                                                .articleCtgr(article.getArticleCtgr())
                                                .articleTitle(article.getArticleTitle())
                                                .articleCreatedDate(article.getArticleCreatedDate())
                                                .build())
                                .collect(Collectors.toList());
        }

        // 내가 작성한 댓글 조회
        @Transactional(readOnly = true)
        public List<ViewMyCommentsResponse> getUserComments(String userId) {
                List<Comment> comments = boardCommentService.findCommentByUserId(userId);
                return comments.stream()
                                .map(comment -> ViewMyCommentsResponse.builder()
                                                .articleContents(comment.getArticle().getArticleContents())
                                                .commentContents(comment.getCommentContents())
                                                .commentCreatedDate(comment.getCommentCreatedDate())
                                                .build())
                                .collect(Collectors.toList());

        }

        // 내가 좋아요한 글 조회
        @Transactional(readOnly = true)
        public List<ViewMyArticleResponse> getUserLikesArticles(String userId) {
                List<Article> articles = boardArticleService.findArticleByUserLikesArticle(userId);
                return articles.stream()
                                .map(article -> ViewMyArticleResponse.builder()
                                                .articleId(article.getArticleId())
                                                .articleCtgr(article.getArticleCtgr())
                                                .articleTitle(article.getArticleTitle())
                                                .articleCreatedDate(article.getArticleCreatedDate())
                                                .build())
                                .collect(Collectors.toList());
        }

}
