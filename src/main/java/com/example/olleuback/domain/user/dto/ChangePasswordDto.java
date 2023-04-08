package com.example.olleuback.domain.user.dto;

import lombok.Data;

@Data
public class ChangePasswordDto {
    private Long id;
    private String newPassword;
}
