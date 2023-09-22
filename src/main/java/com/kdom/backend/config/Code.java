package com.kdom.backend.config;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum Code {

    SUCCESS(HttpStatus.OK, true, 2000, "요청에 성공하였습니다.");


    private final HttpStatus httpStatus;
    private final boolean isSuccess;
    private final int code;
    private final String message;

    Code(HttpStatus httpStatus, boolean isSuccess, int code, String message) {
        this.httpStatus = httpStatus;
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }

}
