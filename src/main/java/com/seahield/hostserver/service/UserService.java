package com.seahield.hostserver.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.seahield.hostserver.config.jwt.TokenProvider;
import com.seahield.hostserver.domain.Article;
import com.seahield.hostserver.domain.Comment;
import com.seahield.hostserver.domain.Company;
import com.seahield.hostserver.domain.User;
import com.seahield.hostserver.domain.UserType;
import com.seahield.hostserver.dto.ArticleDto.ViewMyArticleResponse;
import com.seahield.hostserver.dto.CommentDto.ViewMyCommentsResponse;
import com.seahield.hostserver.dto.UserDto.EditUserInfoRequest;
import com.seahield.hostserver.dto.UserDto.ViewUserInfoResponse;
import com.seahield.hostserver.dto.UserDto.ViewUsersInfoResponse;
import com.seahield.hostserver.exception.ErrorException;
import com.seahield.hostserver.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

	private final TokenProvider tokenProvider;
	private final BoardArticleService boardArticleService;
	private final BoardCommentService boardCommentService;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final UserRepository userRepository;

	// 사용자 타입 출력
	public UserType findUserType(String userId) {
		return this.findByUserId(userId).getUserType();
	}

	// 아이디로 회원 찾기
	@Cacheable(value = "userId", key = "#userId")
	public User findByUserId(String userId) {
		if (userRepository.findByUserId(userId) == null) {
			throw new ErrorException("NOT FOUND ID");
		} else {
			return userRepository.findByUserId(userId);
		}
	}

	// 이메일로 회원 찾기
	@Cacheable(value = "userEmail", key = "#userEmail")
	public User findByUserEmail(String email) {
		if (userRepository.findByUserEmail(email) == null) {
			throw new ErrorException("NOT FOUND EMAIL");
		} else {
			return userRepository.findByUserEmail(email).orElseThrow();
		}
	}

	// 회원 정보 조회
	@Transactional(readOnly = true)
	public ViewUserInfoResponse getUserInfo(String accessToken) {
		String userId = tokenProvider.getUserId(accessToken);
		User user = this.findByUserId(userId);
		Company company = user.getCompany();
		if (company != null) {
			String companyRegistNum = company.getCompanyRegistNum();
			return ViewUserInfoResponse.builder()
					.userId(user.getUserId())
					.userNickname(user.getUserNickname())
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
					.userNickname(user.getUserNickname())
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
	@CacheEvict(value = "userId", key = "#userId")
	@CachePut(value = "userId", key = "#userId")
	public void editUserInfo(String userId, EditUserInfoRequest request) {
		// String userId = tokenProvider.getUserId(accessToken);
		User user = userRepository.findByUserId(userId);
		// evictUserCache(userId);
		user.setUserInfo(bCryptPasswordEncoder.encode(request.getUserPwd()), request.getUserNickname(),
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

	// 회원 정보 전체 조회(관리자권한)
	public List<ViewUsersInfoResponse> getAllUsersInfo(String userId) {
		User user = this.findByUserId(userId);
		if (user.getUserType() == UserType.ADMIN) {
			List<User> allUsers = userRepository.findAll();
			List<ViewUsersInfoResponse> usersInfo = allUsers.stream().map(member -> ViewUsersInfoResponse.builder()
					.userId(member.getUserId())
					.userNickname(member.getUserNickname())
					.userEmail(member.getUserEmail())
					.userContact(member.getUserContact())
					.userType(member.getUserType().getDescription())
					.build()).collect(Collectors.toList());
			return usersInfo;

		} else {
			throw new ErrorException("NO PERMISSION");
		}
	}

}
