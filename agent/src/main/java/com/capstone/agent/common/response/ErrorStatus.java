package com.capstone.agent.common.response;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access =  AccessLevel.PROTECTED)
public enum ErrorStatus {
    /* 400 BAD_REQUEST */
    VALIDATION_REQUEST_MISSING_EXCEPTION(HttpStatus.BAD_REQUEST, "요청 값이 입력되지 않았습니다."),
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "중복된 이메일입니다."),
    MISSING_EMAIL(HttpStatus.BAD_REQUEST,"이메일이 입력되지 않았습니다."),

    /* 401 UNAUTHORIZED */
    USER_UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"인증되지 않은 사용자입니다."),
    INVALID_ACCESSTOKEN_EXCEPTION(HttpStatus.UNAUTHORIZED, "유효하지 않은 엑세스토큰입니다."),
    INVALID_REFRESHTOKEN_EXCEPTION(HttpStatus.UNAUTHORIZED, "유효하지 않은 리프레시토큰입니다."),

    /* 404 NOT_FOUND */
    USER_NOTFOUND_EXCEPTION(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다."),

    /* 500 SERVER_ERROR */
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),
    FAIL_SAVE_USER_INFO(HttpStatus.INTERNAL_SERVER_ERROR, "사용자 정보 저장을 실패했습니다"),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    public int getStatusCode() {
        return this.httpStatus.value();
    }
}
