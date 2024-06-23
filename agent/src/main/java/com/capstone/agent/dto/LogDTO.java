package com.capstone.agent.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
public class LogDTO {
    private String role;
    private String chat;
    private LocalDateTime created_at;

    @Builder
    public LogDTO(String role, String chat, LocalDateTime created_at) {
        this.role = role;
        this.chat = chat;
        this.created_at = created_at;
    }
}
