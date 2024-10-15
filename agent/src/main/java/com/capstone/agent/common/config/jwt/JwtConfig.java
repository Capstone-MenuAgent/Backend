package com.capstone.agent.common.config.jwt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.capstone.agent.api.member.jwt.filter.JwtAuthenticationProcessingFilter;
import com.capstone.agent.api.member.jwt.service.JwtService;
import com.capstone.agent.api.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class JwtConfig {

    private final JwtService jwtService;
    private final MemberRepository memberRepository;

    @Bean
    public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() {
        return new JwtAuthenticationProcessingFilter(jwtService, memberRepository);
    }
}
