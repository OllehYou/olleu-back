package com.example.olleuback.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(OlleUException.class)
    public ResponseEntity<ExceptionResponse> olleUExceptionHandler(OlleUException ex) {
        ExceptionResponse response
                = new ExceptionResponse(ex.getCode(), ex.getMessage(), ex.getStatus());
        return ResponseEntity.status(ex.getStatus()).body(response);
    }
}
