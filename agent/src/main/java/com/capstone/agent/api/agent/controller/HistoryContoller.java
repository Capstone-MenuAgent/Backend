package com.capstone.agent.api.agent.controller;
import com.capstone.agent.api.agent.service.HistoryService;
import com.capstone.agent.api.member.jwt.service.JwtService;
import com.capstone.agent.api.member.service.MemberService;

import jakarta.servlet.http.HttpServletRequest;

import com.capstone.agent.api.agent.dto.HistoryRequestDTO;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/history")
public class HistoryContoller {
    private final HistoryService historyService;
    private final JwtService jwtService;
    private final MemberService memberService;

    @GetMapping("/info")
    public HistoryRequestDTO loadHistory(HttpServletRequest request) {
        String accessToken = jwtService.extractAccessToken(request)
                .filter(jwtService::isTokenValid)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 Access Token"));
    
        Long userId = memberService.memberInfo(jwtService.extractEmail(accessToken)
                .orElseThrow(() -> new RuntimeException("이메일 추출 실패"))
        ).getId();
        
        HistoryRequestDTO historyRequest = HistoryRequestDTO.builder()
                .memberId(userId)
                .historyData(historyService.loadHistory(userId))
                .build();
        
        return historyRequest;
    }
}