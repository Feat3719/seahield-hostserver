package com.seahield.hostserver.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "COMPANY")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Company implements UserDetails {

    @Id
    @Column(name = "company_id", nullable = false)
    private String companyId;

    @Column(name = "company_pwd", nullable = false)
    private String companyPwd;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "company_email", nullable = false)
    private String comapanyEmail;

    @Column(name = "company_contact", nullable = false)
    private String companyContact;

    @Column(name = "company_address", nullable = false)
    private String companyAddress;

    @Column(name = "company_joined_ymd", nullable = false)
    @CreatedBy
    private LocalDate companyJoinedYmd;

    @LastModifiedDate
    @Column(name = "company_update_ymd")
    private LocalDateTime companyUpdateYmd;

    // 비밀번호 찾기 => 비밀번호 초기화 및 재설정 관련 메소드
    public void updatePassword(String newPassword) {
        this.companyPwd = newPassword;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));
    }

    @Override
    public String getUsername() {
        return companyId;
    }

    @Override
    public String getPassword() {
        return companyPwd;
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
