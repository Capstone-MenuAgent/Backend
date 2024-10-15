package com.capstone.agent.api.member.controller;

import com.capstone.agent.api.member.dto.MemberInfoResponseDTO;
import com.capstone.agent.api.member.dto.SignupRequestDTO;
import com.capstone.agent.api.member.service.MemberService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/signup")
    public HttpStatus signup(@RequestBody SignupRequestDTO signupRequest) {
        memberService.signup(signupRequest);
        
        return HttpStatus.OK;
    }

    @PostMapping("/quit")
    public HttpStatus quitMember(@RequestParam Long userId) {
        memberService.quitMember(userId);
        
        return HttpStatus.OK;
    }

    @GetMapping("/memberInfo")
    public MemberInfoResponseDTO memberInfo(@RequestParam Long userId) {
        MemberInfoResponseDTO memberInfo = memberService.memberInfo(userId);
        return memberInfo;
    }
}