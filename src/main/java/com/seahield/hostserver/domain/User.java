package com.seahield.hostserver.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "USER")
@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User implements UserDetails {

    @Id
    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "user_pwd", nullable = false)
    private String userPwd;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "user_email", nullable = false)
    private String userEmail;

    @Column(name = "user_contact", nullable = false)
    private String userContact;

    @Column(name = "user_address", nullable = false)
    private String userAddress;

    @Column(name = "user_type", nullable = false)
    private String userType;

    @Column(name = "company_registration_num")
    private String companyRegistNum;

    @Column(name = "user_joined_ymd")
    @CreatedDate
    private LocalDate userJoinedYmd;

    @LastModifiedDate
    @Column(name = "user_update_ymd")
    private LocalDateTime userUpdateYmd;

    @OneToMany(mappedBy = "qnaArticleWriter")
    private List<QnaArticle> qnaArticles;

    @OneToMany(mappedBy = "qnaCommentWriter")
    private List<QnaComment> qnaBoardComments;

    // 비밀번호 찾기 => 비밀번호 초기화 및 재설정 관련 메소드
    public void updatePassword(String newPassword) {
        this.userPwd = newPassword;
    }

    // 회원가입 Builder
    @Builder
    public User(
            String userId,
            String userPwd,
            String userName,
            String userEmail,
            String userContact,
            String userAddress,
            String userType,
            LocalDate userJoinedYmd,
            LocalDateTime userUpdateYmd) {
        this.userId = userId;
        this.userPwd = userPwd;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userContact = userContact;
        this.userAddress = userAddress;
        this.userType = userType;
        this.userJoinedYmd = userJoinedYmd;
        this.userUpdateYmd = userUpdateYmd;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));
    }

    @Override
    public String getUsername() {
        return userId;
    }

    @Override
    public String getPassword() {
        return userPwd;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
