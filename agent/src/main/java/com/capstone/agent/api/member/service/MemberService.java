package com.capstone.agent.api.member.service;

import com.capstone.agent.api.member.dto.MemberInfoDTO;
import com.capstone.agent.api.member.dto.SignupRequestDTO;
import com.capstone.agent.api.member.entity.Member;
import com.capstone.agent.api.member.entity.Role;
import com.capstone.agent.api.member.repository.MemberRepository;
import com.capstone.agent.common.response.ErrorStatus;
import com.capstone.agent.common.exception.BadRequestException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signup(SignupRequestDTO signupRequest) {
        Member rawMember = Member.builder()
                            .email(signupRequest.getEmail())
                            .password(signupRequest.getPassword())
                            .name(signupRequest.getName())
                            .addr(signupRequest.getAddr())
                            .age(signupRequest.getAge())
                            .gender(signupRequest.getGender())
                            .role(Role.USER)
                            .build();
        Member member = rawMember.passwordEncoder(passwordEncoder);

        // 존재하는 이메일인지 확인
        checkEmail(member.getEmail());
        try {
            memberRepository.save(member);
            //
        } catch (BadRequestException e) {
            log.error(ErrorStatus.FAIL_SAVE_USER_INFO.getMessage(), e);
            throw new BadRequestException(ErrorStatus.FAIL_SAVE_USER_INFO.getMessage());
        }
        
    }

    @Transactional
    public void checkEmail(String email) {
        validEmail(email);
    }

    private void validEmail(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new BadRequestException(ErrorStatus.DUPLICATE_EMAIL.getMessage());
        }
    }

    @Transactional
    public void quitMember(Long userId) {
        Member member = memberRepository.findById(userId)
                            .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다"));
        memberRepository.delete(member);
    }

    @Transactional
    public MemberInfoDTO memberInfo(Long userId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다"));

        MemberInfoDTO memberInfoDTO = MemberInfoDTO.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .addr(member.getAddr())
                .age(member.getAge())
                .gender(member.getGender())
                .role(member.getRole())
                .build();
        return memberInfoDTO;
    }

    @Transactional
    public MemberInfoDTO memberInfo(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다"));

        MemberInfoDTO memberInfoDTO = MemberInfoDTO.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .addr(member.getAddr())
                .age(member.getAge())
                .gender(member.getGender())
                .role(member.getRole())
                .build();
        return memberInfoDTO;
    }

    @Transactional
    public void modify(MemberInfoDTO memberInfoDTO) {
        Member member = memberRepository.findByEmail(memberInfoDTO.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 멤버"))
                .toBuilder()
                        .name(memberInfoDTO.getName())
                        .addr(memberInfoDTO.getAddr())
                        .age(memberInfoDTO.getAge())
                        .gender(memberInfoDTO.getGender())
                        .build();

        memberRepository.save(member);
    }
}