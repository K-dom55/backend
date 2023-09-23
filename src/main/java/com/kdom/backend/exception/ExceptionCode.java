package com.kdom.backend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionCode {

    SUCCESS(HttpStatus.OK, true, 2000, "요청에 성공하였습니다."),

    MEMBER_IP_NOT_FOUND(HttpStatus.NOT_FOUND, false, 3000, "해당 아이피를 가진 회원이 없습니다."),
    ARTICLE_ID_NOT_FOUND(HttpStatus.NOT_FOUND, false, 3001, "해당 아이디를 가진 게시글이 없습니다."),
    LIKE_USER_IP_CONFLICT(HttpStatus.CONFLICT, false, 3002, "이미 좋아요를 눌렀던 사용자입니다."),
    FILE_UPLOAD_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, false, 3003,"파일 업로드에 실패했습니다."),

    /*
    * 재은: 4000번 ~ 6000 번까지 사용
    * */
    IOEXCEPTION(HttpStatus.BAD_REQUEST, false, 4000, "입출력 과정에서 문제가 발생했습니다."),

    ERRARTICLEREPO(HttpStatus.BAD_REQUEST, false, 4001, "글을 작성하는 Service로직이 정상적으로 이루어지지 않았습니다."),
    EMPTYS3URL(HttpStatus.BAD_REQUEST, false, 4002, "S3가 정상적으로 생성되지 않았습니다."),

    EMPTYARTICLEDTO(HttpStatus.BAD_REQUEST, false, 4003, "ARTICLEDTO가 정상적ㅇ로 생성되지 않았습니다. "),

    EMPTYARTICLE(HttpStatus.BAD_REQUEST, false, 4004, "ARTICLE을 찾을 수 없음"),

    EMPTYHASHTAG(HttpStatus.BAD_REQUEST, false, 4004, "HASHTAG를 찾을 수 없음"),

    /*
    * 소현: 6000~ 7000
    * */

    NOMOREARTICLE(HttpStatus.OK, true, 6000,  "조회할 리스트가 더이상 없습니다.");


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
