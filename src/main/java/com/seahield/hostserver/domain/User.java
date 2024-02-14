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

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "USER", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id", unique = true)
})
@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User implements UserDetails {

    @Id
    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "user_pwd")
    private String userPwd;

    @Column(name = "user_nickname")
    private String userNickname;

    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "user_contact", nullable = false)
    private String userContact;

    @Column(name = "user_address")
    private String userAddress;

    @Column(name = "user_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserType userType;

    @CreatedDate
    @Column(name = "user_joined_ymd")
    private LocalDate userJoinedYmd;

    @LastModifiedDate
    @Column(name = "user_update_ymd")
    private LocalDateTime userUpdateYmd;

    @OneToMany(mappedBy = "articleWriter", cascade = CascadeType.REMOVE)
    private List<Article> Articles;

    @OneToMany(mappedBy = "commentWriter", cascade = CascadeType.REMOVE)
    private List<Comment> Comments;

    @OneToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
    @JoinColumn(name = "company_regist_num", referencedColumnName = "company_regist_num")
    private Company company;

    // 비밀번호 찾기 => 비밀번호 초기화 및 재설정 관련 메소드
    public void updatePassword(String newPassword) {
        this.userPwd = newPassword;
    }

    // 회원가입 Builder
    @Builder
    public User(
            String userId,
            String userPwd,
            String userNickname,
            String userEmail,
            String userContact,
            String userAddress,
            UserType userType,
            Company company,
            LocalDate userJoinedYmd,
            LocalDateTime userUpdateYmd) {
        this.userId = userId;
        this.userPwd = userPwd;
        this.userNickname = userNickname;
        this.userEmail = userEmail;
        this.userContact = userContact;
        this.userAddress = userAddress;
        this.userType = userType;
        this.userJoinedYmd = userJoinedYmd;
        this.userUpdateYmd = userUpdateYmd;
        this.company = company;
    }

    // 회원 정보 수정
    public void setUserInfo(String userPwd, String userNickname, String userAddress) {
        this.userPwd = userPwd;
        this.userNickname = userNickname;
        this.userAddress = userAddress;
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
