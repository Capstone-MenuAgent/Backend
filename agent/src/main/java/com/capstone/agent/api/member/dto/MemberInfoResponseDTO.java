package com.capstone.agent.api.member.dto;

import com.capstone.agent.api.member.entity.Gender;
import com.capstone.agent.api.member.entity.Role;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberInfoResponseDTO {
    private final Long id;
    private final String email;
    private final String name;
    private final String addr;
    private final int age;
    private final Gender gender;
    private final Role role;
    
}