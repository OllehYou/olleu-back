package com.example.olleuback.domain.user.dto;

import lombok.Data;

@Data
public class AuthCodeConfirmDto {
    private Long id;
    private String authCode;
}
