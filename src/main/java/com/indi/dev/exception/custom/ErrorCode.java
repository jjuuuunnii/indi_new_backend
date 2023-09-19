package com.indi.dev.exception.custom;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    //400
    LOGIN_FAILED(HttpStatus.BAD_REQUEST, "LOGIN FAILED"),
    INVALID_COMPLETION_DATE(HttpStatus.BAD_REQUEST, "INVALID_COMPLETION_DATE"),
    INVALID_DATE(HttpStatus.BAD_REQUEST, "INVALID_DATE"),
    //401
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "INVALID TOKEN"),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "TOKEN_EXPIRED"),
    INVALID_SIGNATURE(HttpStatus.UNAUTHORIZED, "INVALID_SIGNATURE"),

    //404
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER NOT FOUND"),
    QUESTION_NOT_FOUND(HttpStatus.NOT_FOUND, "QUESTION NOT FOUND"),
    USER_QUESTION_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_QUESTION_NOT_FOUND"),
    VIDEO_NOT_FOUND(HttpStatus.NOT_FOUND, "VIDEO NOT FOUND"),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMENT NOT FOUND"),

    //500
    INVALID_REQUEST(HttpStatus.INTERNAL_SERVER_ERROR, "INVALID REQUEST"),
    ;


    private final HttpStatus httpStatus;
    private final String code;

    ErrorCode(HttpStatus httpStatus, String code) {
        this.httpStatus = httpStatus;
        this.code = code;
    }
}
