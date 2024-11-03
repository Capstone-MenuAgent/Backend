package com.capstone.agent.api.member.dto;

import com.capstone.agent.api.member.entity.Gender;
import com.capstone.agent.api.member.entity.Role;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberInfoDTO {
    private Long id;
    private String email;
    private String name;
    private String addr;
    private int age;
    private Gender gender;
    private Role role;
}