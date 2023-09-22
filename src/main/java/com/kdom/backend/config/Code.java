package com.kdom.backend.config;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum Code {

    SUCCESS(HttpStatus.OK, true, 2000, "요청에 성공하였습니다."),

    IOEXCEPTION(HttpStatus.BAD_REQUEST, false, 3000, "입출력 과정에서 문제가 발생했습니다."),


    ERRARTICLEREPO(HttpStatus.BAD_REQUEST, false, 4001, "글을 작성하는 Service로직이 정상적으로 이루어지지 않았습니다."),
    EMPTYS3URL(HttpStatus.BAD_REQUEST, false, 4002, "S3가 정상적으로 생성되지 않았습니다.");
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
