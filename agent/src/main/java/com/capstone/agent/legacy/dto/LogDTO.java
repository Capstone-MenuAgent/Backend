package com.capstone.agent.legacy.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Data;

@Data
public class LogDTO {
    private String role;
    private String chat;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime created_at;

    @Builder
    public LogDTO(String role, String chat, LocalDateTime created_at) {
        this.role = role;
        this.chat = chat;
        this.created_at = created_at;
    }
}
