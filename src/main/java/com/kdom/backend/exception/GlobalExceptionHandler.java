package com.kdom.backend.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<BaseResponse> businessExceptionHandler(BusinessException ex) {
        return ResponseEntity
                .status(ex.getExceptionCode().getHttpStatus())
                .body(new BaseResponse(ex.getExceptionCode()));
    }
}
