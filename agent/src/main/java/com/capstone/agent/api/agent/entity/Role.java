package com.capstone.agent.api.agent.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    HUMAN("ROLE_HUMAN"), COMPUTER("ROLE_COMPUTER");
    private final String key;
}