package com.kdom.backend.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private ExceptionCode exceptionCode;

    public BusinessException(ExceptionCode code) {
        super(code.getMessage());
        this.exceptionCode = code;
    }
}
