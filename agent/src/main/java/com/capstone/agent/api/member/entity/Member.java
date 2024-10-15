package com.capstone.agent.api.member.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Entity
@Builder(toBuilder = true)
@Table(name = "members")
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;
    private String password;

    private String name;
    private String addr;
    private int age;
    
    private String refreshToken; // JWT 리프레시 토큰

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Role role;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    protected LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    protected LocalDateTime updatedAt;

    public Member authorizeMember() {
        return this.toBuilder()
                    .role(Role.USER)
                    .build();
    }

    public Member passwordEncoder(PasswordEncoder passwordEncoder) {
        return this.toBuilder()
                .password(passwordEncoder.encode(this.password))
                .build();
    }

    // refresh token 업데이트
    public Member updateRefreshToken(String updateRefreshToken) {
        return this.toBuilder()
                .refreshToken(updateRefreshToken)
                .build();
    }
}