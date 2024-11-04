package com.capstone.agent.api.agent.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuestionDTO {
    private String query;
    private String location;
    private String gender;
    private int age;
}
