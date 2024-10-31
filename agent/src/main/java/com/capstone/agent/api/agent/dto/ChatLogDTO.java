package com.capstone.agent.api.agent.dto;
import com.capstone.agent.api.agent.entity.Role;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatLogDTO {
    private Long memberId;
    private String log;
    private Role role;
}
