package com.example.olleuback.domain.user.dto;

import lombok.Data;

@Data
public class CreateUserDto {
    private String email;
    private String nickname;
    private String password;
}
