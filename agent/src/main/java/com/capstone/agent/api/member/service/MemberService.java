package com.capstone.agent.api.member.service;

import com.capstone.agent.api.member.dto.MemberInfoResponseDTO;
import com.capstone.agent.api.member.dto.SignupRequestDTO;
import com.capstone.agent.api.member.entity.Member;
import com.capstone.agent.api.member.entity.Role;
import com.capstone.agent.api.member.repository.MemberRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import lombok.RequiredArgsConstructor;

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
        validateDuplicateMember(member);
        memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member) {
        memberRepository.findByEmail(member.getEmail())
                .ifPresent(m -> {
                    throw new IllegalArgumentException("이미 존재하는 이메일입니다: " + member.getEmail());
                });
    }

    @Transactional
    public void quitMember(Long userId) {
        Member member = memberRepository.findById(userId)
                            .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다"));
        memberRepository.delete(member);
    }

    @Transactional
    public MemberInfoResponseDTO memberInfo(Long userId) {
        Member member = memberRepository.findById(userId)
                            .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다"));
        MemberInfoResponseDTO memberInfoResponse = MemberInfoResponseDTO.builder()
                                                        .id(member.getId())
                                                        .email(member.getEmail())
                                                        .name(member.getName())
                                                        .addr(member.getAddr())
                                                        .age(member.getAge())
                                                        .gender(member.getGender())
                                                        .role(member.getRole())
                                                        .build();
        return memberInfoResponse;
    }
}