package com.capstone.agent.dto;


import lombok.Builder;
import lombok.Data;

@Data
public class SignupDTO {
    private String userEmail;
    private String userPassword;
    private String name;
    private int age;
    private String gender;
    private String addr;
    
    @Builder
    public SignupDTO(String email, String password,
                     String name, int age,
                     String gender, String address) {
        this.userEmail = email;
        this.userPassword = password;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.addr = address;
    }
}