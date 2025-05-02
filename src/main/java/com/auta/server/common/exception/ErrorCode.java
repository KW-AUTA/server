package com.auta.server.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),
    PROJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 프로젝트입니다."),
    DUPLICATED_DB_EMAIL(HttpStatus.CONFLICT, "db의 유저 이메일이 중복됩니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 틀렸습니다"),
    DUPLICATE_USERNAME(HttpStatus.BAD_REQUEST, "이름이 중복됩니다."),
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "이메일이 중복됩니다.");


    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
