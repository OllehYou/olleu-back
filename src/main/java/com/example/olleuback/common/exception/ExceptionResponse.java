package com.example.olleuback.common.exception;

import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
public record ExceptionResponse(Integer code, String message, HttpStatus status) {
}
