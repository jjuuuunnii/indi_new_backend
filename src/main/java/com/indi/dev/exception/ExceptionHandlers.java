package com.indi.dev.exception;

import com.indi.dev.dto.exception.ErrorDto;
import com.indi.dev.exception.custom.CustomException;
import com.indi.dev.exception.jwt.InvalidAccessTokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler({CustomException.class})
    public ResponseEntity<ErrorDto> handleCustomException(CustomException e) {
        log.error("error = {}", e.getErrorCode().getCode());
        ErrorDto errorDto = ErrorDto.builder().code(e.getErrorCode().getCode()).build();
        return new ResponseEntity<>(errorDto, e.getErrorCode().getHttpStatus());
    }

    @ExceptionHandler({InvalidAccessTokenException.class})
    public ResponseEntity<ErrorDto> handleInvalidAccessTokenException(InvalidAccessTokenException e) {
        log.error("error = {}", e.getMessage());
        ErrorDto errorDto = ErrorDto.builder().code(e.getMessage()).build();
        return new ResponseEntity<>(errorDto, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({NumberFormatException.class})
    public ResponseEntity<ErrorDto> handleNumberFormatException(NumberFormatException e){
        log.error("error = {}", e.getMessage());
        ErrorDto errorDto = ErrorDto.builder().code(e.getMessage()).build();
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

}
