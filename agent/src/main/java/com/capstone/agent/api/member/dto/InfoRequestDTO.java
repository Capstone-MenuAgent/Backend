package com.capstone.agent.api.member.dto;

import com.capstone.agent.api.member.entity.Gender;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InfoRequestDTO {
    private String name;
    private String addr;
    private int age;
    private Gender gender;
}
