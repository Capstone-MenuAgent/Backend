package com.capstone.agent.common.response;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum SuccessStatus {
    /* 200 */
    SEND_ACCESSTOKEN_SUCCESS(HttpStatus.OK,"엑세스토큰 발급 성공"),
    SEND_LOGIN_SUCCESS(HttpStatus.OK, "로그인 성공"),

    CREATE_USER_SUCCESS(HttpStatus.OK, "회원가입 성공"),
    CHECK_EMAIL_SUCCESS(HttpStatus.OK, "이메일 사용 가능"),

    GET_USERINFO_SUCCESS(HttpStatus.OK,"사용자 정보 조회 성공"),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    public int getStatusCode() {
        return this.httpStatus.value();
    }
}
