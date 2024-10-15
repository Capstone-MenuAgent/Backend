package com.capstone.agent.api.member.jwt.service;

import com.capstone.agent.api.member.entity.Member;
import com.capstone.agent.api.member.repository.MemberRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@Getter
@RequiredArgsConstructor
public class JwtService {
    @Value("${jwt.key.secretKey}")
    private String secretKey;

    @Value("${jwt.access.expiration}")
    private Long accessTokenExpirationPeriod;
    
    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpirationPeriod;
    
    @Value("${jwt.access.header}")
    private String accessHeader;

    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String EMAIL_CLAIM = "email";
    private static final String BEARER = "Bearer";

    private final MemberRepository memberRepository;

    // Access Token 생성하기
    public String createAccessToken(String email) {
        Date now = new Date();
        return JWT.create()
                .withSubject(ACCESS_TOKEN_SUBJECT)
                .withExpiresAt(new Date(now.getTime() + accessTokenExpirationPeriod))
                .withClaim(EMAIL_CLAIM, email)
                .sign(Algorithm.HMAC512(secretKey)); // 512는 최소 64 bytes, 권장 128 bytes
    }

    // Refresh Token 생성하기
    public String createRefreshToken() {
        Date now = new Date();
        return JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT)
                .withExpiresAt(new Date(now.getTime() + refreshTokenExpirationPeriod))
                .sign(Algorithm.HMAC512(secretKey));
    }

    public Map<String, String> createAccessAndRefreshToken(String email) {
        String accessToken = createAccessToken(email);
        String refreshToken = createRefreshToken();

        // Refresh Token 업데이트
        updateRefreshToken(email, refreshToken);
        
        return Map.of(
            "accessToken", accessToken,
            "refreshToken", refreshToken
        );
    }

    public void sendAccessToken(HttpServletResponse response, String accessToken, String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader(accessHeader, accessToken);
        log.info("Access Token 재발급 : {}", accessToken);
    }

    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader(accessHeader, accessToken);
        response.setHeader(refreshHeader, refreshToken);
        log.info("Access Token, Refresh Token Header 완료");
    }

    // 헤더에서 Access Token 추출
    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(accessHeader))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""));
    }
    // 헤더에서 Refresh Token 추출
    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(refreshHeader))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""));
    }

    public Optional<String> extractEmail(String accessToken) {
        try {
            return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
                    .build()
                    .verify(accessToken)
                    .getClaim(EMAIL_CLAIM)
                    .asString());
        } catch (Exception e) {
            log.error("유효하지 않은 Access Token");
            return Optional.empty();
        }
    }

    @Transactional
    public void updateRefreshToken(String email, String refreshToken) {
        memberRepository.findByEmail(email).ifPresentOrElse(
                member -> {
                        Member updatedMember = member.updateRefreshToken(refreshToken);
                        memberRepository.save(updatedMember);
                },
                () -> new Exception("일치하는 회원이 없습니다")
        );
    }

    public boolean isTokenValid(String token) {
        try {
            JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
            return true;
        } catch (Exception e) {
            log.error("유효하지 않은 토큰 {}", e.getMessage());
            return false;
        }
    }
}
