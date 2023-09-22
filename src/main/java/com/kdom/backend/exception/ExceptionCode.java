package com.kdom.backend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionCode {

    SUCCESS(HttpStatus.OK, true, 2000, "요청에 성공하였습니다."),

    MEMBER_IP_NOT_FOUND(HttpStatus.NOT_FOUND, false, 3000, "해당 아이피를 가진 회원이 없습니다."),
    ARTICLE_ID_NOT_FOUND(HttpStatus.NOT_FOUND, false, 3001, "해당 아이디를 가진 게시글이 없습니다."),
    LIKE_USER_IP_CONFLICT(HttpStatus.CONFLICT, false, 3002, "이미 좋아요를 눌렀던 사용자입니다.");


    private final HttpStatus httpStatus;
    private final boolean isSuccess;
    private final int code;
    private final String message;

    ExceptionCode(HttpStatus httpStatus, boolean isSuccess, int code, String message) {
        this.httpStatus = httpStatus;
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }

}
