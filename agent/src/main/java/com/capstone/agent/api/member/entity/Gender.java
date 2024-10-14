package com.capstone.agent.api.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Gender {
    MAN("GENDER_MAN"), WOMEN("GENDER_WOMEN"), NONE("GENDER_NONE");
    private final String gender;
}