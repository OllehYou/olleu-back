package com.example.olleuback.common.exception;

import org.springframework.http.HttpStatus;

public record ExceptionResponse(Integer code, String message, HttpStatus status) {
}
