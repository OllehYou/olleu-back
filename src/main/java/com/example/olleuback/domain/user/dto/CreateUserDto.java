package com.example.olleuback.domain.user.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class CreateUserDto {
    private String email;
    private String nickname;
    private String password;
    private LocalDate localDate;
}
