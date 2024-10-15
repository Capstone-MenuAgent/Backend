package com.capstone.agent.api.member.service;

import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.capstone.agent.api.member.dto.LoginRequestDTO;
import com.capstone.agent.api.member.dto.MemberInfoResponseDTO;
import com.capstone.agent.api.member.dto.SignupRequestDTO;
import com.capstone.agent.api.member.entity.Member;
import com.capstone.agent.api.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public void signup(SignupRequestDTO signupRequest) {
        Member member = Member.builder()
                            .email(signupRequest.getEmail())
                            .password(signupRequest.getPassword())
                            .name(signupRequest.getName())
                            .addr(signupRequest.getAddr())
                            .age(signupRequest.getAge())
                            .gender(signupRequest.getGender())
                            .build();
        validateDuplicateMember(member);
        memberRepository.save(member);

        // 유저에게 권한 부여
        Member updatedMember = member.authorizeMember();
        memberRepository.save(updatedMember);
    }

    private void validateDuplicateMember(Member member) {
        memberRepository.findByEmail(member.getEmail())
                .ifPresent(m -> {
                    throw new IllegalArgumentException("이미 존재하는 이메일입니다: " + member.getEmail());
                });
    }

    @Transactional
    public Long login(LoginRequestDTO loginRequest) { // security config 작성 예정
        Member member = memberRepository.findByEmail(loginRequest.getEmail()) // 추후 변경 예정
                            .orElseThrow(() -> new NoSuchElementException("사용자를 찾을 수 없습니다"));
        if (!member.getPassword().equals(loginRequest.getPassword())) {       // 추후 변경 예정
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
        }
        return member.getId();
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