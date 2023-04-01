package com.example.olleuback.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class OlleUException extends RuntimeException{
    private Integer code;
    private String message;
    private HttpStatus status;
}
