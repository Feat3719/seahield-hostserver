package com.seahield.hostserver.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.seahield.hostserver.config.jwt.TokenProvider;
import com.seahield.hostserver.domain.User;
import com.seahield.hostserver.dto.UserDto.EditUserInfoRequest;
import com.seahield.hostserver.dto.UserDto.ViewUserInfoResponse;
import com.seahield.hostserver.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final TokenProvider tokenProvider;
    private final AuthService authService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;

    // 유저 정보 조회
    public ViewUserInfoResponse getUserInfo(String accessToken) {
        String userId = tokenProvider.getUserId(accessToken);
        User user = authService.findByUserId(userId);
        return ViewUserInfoResponse.builder()
                .userId(user.getUserId())
                .userName(user.getUsername())
                .userEmail(user.getUserEmail())
                .userAddress(user.getUserAddress())
                .userType(user.getUserType().getDescription())
                .companyRegistNum(user.getCompanyRegistNum())
                .userJoinedYmd(user.getUserJoinedYmd())
                .build();
    }

    // 유저 정보 수정
    @Transactional
    public void editUserInfo(String accessToken, EditUserInfoRequest request) {
        String userId = tokenProvider.getUserId(accessToken);
        User user = authService.findByUserId(userId);
        user.setUserInfo(bCryptPasswordEncoder.encode(request.getUserPwd()), request.getUserName(),
                request.getUserEmail(), request.getUserAddress());
        userRepository.save(user);
    }

}
