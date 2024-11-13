package com.capstone.agent.api.member.controller;

import com.capstone.agent.api.member.dto.MemberInfoDTO;
import com.capstone.agent.api.member.dto.InfoRequestDTO;
import com.capstone.agent.api.member.dto.InfoResponseDTO;
import com.capstone.agent.api.member.dto.SignupRequestDTO;
import com.capstone.agent.api.member.jwt.service.JwtService;
import com.capstone.agent.api.member.service.MemberService;
import com.capstone.agent.common.response.ApiResponse;
import com.capstone.agent.common.exception.BadRequestException;
import com.capstone.agent.common.response.ErrorStatus;
import com.capstone.agent.common.response.SuccessStatus;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/member")
public class MemberController {
    private final MemberService memberService;
    private final JwtService jwtService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> signup(@RequestBody SignupRequestDTO signupRequest) {
        memberService.signup(signupRequest);
        return ApiResponse.success_only(SuccessStatus.CREATE_USER_SUCCESS);
    }

    @PostMapping("/check-email")
    public ResponseEntity<ApiResponse<Void>> checkEmail(@RequestParam("email") String email) {
        if (email == null || email.isEmpty()) {
            throw new BadRequestException(ErrorStatus.MISSING_EMAIL.getMessage());
        }
        memberService.checkEmail(email);
        return ApiResponse.success_only(SuccessStatus.CHECK_EMAIL_SUCCESS);
    }

    @GetMapping("/memberInfo")
    public InfoResponseDTO getMemberInfo(HttpServletRequest request) {
        String accessToken = jwtService.extractAccessToken(request)
                .filter(jwtService::isTokenValid)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 Access Token"));
    
        Long userId = memberService.memberInfo(jwtService.extractEmail(accessToken)
                .orElseThrow(() -> new RuntimeException("이메일 추출 실패"))
        ).getId();

        MemberInfoDTO memberInfo = memberService.memberInfo(userId);

        InfoResponseDTO infoResponse = InfoResponseDTO.builder()
                .name(memberInfo.getName())
                .addr(memberInfo.getAddr())
                .age(memberInfo.getAge())
                .gender(memberInfo.getGender())
                .build();

        return infoResponse;
    }

    @PostMapping("/memberInfo")
    public HttpStatus putMemberInfo(@RequestBody InfoRequestDTO InfoRequest, HttpServletRequest request) {
        String accessToken = jwtService.extractAccessToken(request)
                .filter(jwtService::isTokenValid)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 Access Token"));
        String email = jwtService.extractEmail(accessToken)
            .orElseThrow(() -> new RuntimeException("이메일 추출 실패"));

        MemberInfoDTO memberInfoDTO = MemberInfoDTO.builder()
                .email(email)
                .name(InfoRequest.getName())
                .addr(InfoRequest.getAddr())
                .age(InfoRequest.getAge())
                .gender(InfoRequest.getGender())
                .build();
        memberService.modify(memberInfoDTO);
    
        return HttpStatus.OK;
    }
}