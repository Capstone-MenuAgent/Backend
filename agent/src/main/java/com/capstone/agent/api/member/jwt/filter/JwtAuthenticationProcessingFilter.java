package com.capstone.agent.api.member.jwt.filter;

import com.capstone.agent.api.member.entity.Member;
import com.capstone.agent.api.member.jwt.service.JwtService;
import com.capstone.agent.api.member.repository.MemberRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

// JWT 인증 필터
// 미리 허가된 URI 외의 요청 처리
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter { // 클라이언트 요청에 1번 실행
    private static final String NO_CHECK_URL = "/api/v1/login"; // "/login" 요청은 작동 X

    private final JwtService jwtService;
    private final MemberRepository memberRepository;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().equals(NO_CHECK_URL)) {
            filterChain.doFilter(request, response); // "/login 요청은 다음 필터로"
            return; // 현재 필터 종료
        }
        
        String refreshToken = jwtService.extractRefreshToken(request)
                .filter(jwtService::isTokenValid)
                .orElse(null);

        // refresh token이 헤더에 있는 경우는 access token이 만료되어 요청한 경우
        // 따라서 refresh token을 확인 후 일치 시 access token 발급
        if (refreshToken != null) {
            checkRefreshTokenAndReIssueAccessToken(response, refreshToken);
            return;
        }

        // refresh token이 없거나 유효하지 않을 때, access token 검사, 인증 로직
        // access token이 없거나 유효하지 않을 때, 인증 객체가 담기지 않았기 때문에 403
        // access token이 유효하다면, 성공
        if (refreshToken == null) {
            checkAccessTokenAndAuthentication(request, response, filterChain);
        }
    }

    // refresh token으로 유저 검색 및 액세스와 refresh token 재발급
    public void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response, String refershToken) {
        log.info("checkRefreshTokenAndReIssueAccessToken 호출");
        memberRepository.findByRefreshToken(refershToken)
                .ifPresent(member -> {
                    String reIssuedRefreshToken = reIssueRefreshToken(member);
                    jwtService.sendAccessAndRefreshToken(response, jwtService.createAccessToken(member.getEmail()), reIssuedRefreshToken);
                });
    }

    // refresh token 재발급, db에 refresh token 업데이트
    private String reIssueRefreshToken(Member member) {
        String reIssuedRefreshToken = jwtService.createRefreshToken();
        member.updateRefreshToken(reIssuedRefreshToken);
        memberRepository.saveAndFlush(member);
        return reIssuedRefreshToken;
    }

    // access token 체크와 인증 처리
    // request에서 access token 추출 및 검증
    // 유효하면 email 추출 및 멤버 객체 반환
    // 멤버 객체 인증 처리해서 다음 필터로 이동
    public void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("checkAccessTokenAndAuthentication 호출");
        jwtService.extractAccessToken(request)
                .filter(jwtService::isTokenValid)
                .ifPresent(accessToken -> jwtService.extractEmail(accessToken)
                        .ifPresent(email -> memberRepository.findByEmail(email)
                                .ifPresent(this::saveAuthentication)));
        filterChain.doFilter(request, response);
    }

    // 인증 허가
    public void saveAuthentication(Member member) {
        UserDetails userDetailsUser = User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().name())
                .build();
        
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetailsUser, null, authoritiesMapper.mapAuthorities(userDetailsUser.getAuthorities()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Authentication authenticationLogCheck = SecurityContextHolder.getContext().getAuthentication();
        if (authenticationLogCheck != null) {
            log.info("현재 인증된 사용자: {}", authentication.getName());
        } else {
            log.info("인증 정보가 존재하지 않습니다.");
        }
    }

}