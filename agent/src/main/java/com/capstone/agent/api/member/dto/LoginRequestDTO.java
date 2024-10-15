package com.capstone.agent.api.member.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequestDTO {
    private String email;
    private String password;
}