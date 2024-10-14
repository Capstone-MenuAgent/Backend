package com.capstone.agent.legacy.dto;


import lombok.Builder;
import lombok.Data;

@Data
public class SignupDTO {
    private String email;
    private String password;
    private String name;
    private int age;
    private String gender;
    private String addr;
    
    @Builder
    public SignupDTO(String email, String password,
                     String name, int age,
                     String gender, String addr) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.addr = addr;
    }
}