package com.capstone.agent.legacy.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class LoginDTO {
    private String userEmail;
    private String userPassword;
    
    @Builder
    public LoginDTO(String email, String password) {
        this.userEmail = email;
        this.userPassword = password;
    }
}
