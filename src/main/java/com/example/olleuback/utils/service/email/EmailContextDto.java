package com.example.olleuback.utils.service.email;

import lombok.Data;

@Data
public class EmailContextDto {

    private String receiverEmail;
    private String password;
    private String authCode;

    public static EmailContextDto ofCreateResetPasswordInfo(String receiverEmail, String password) {
        EmailContextDto dto = new EmailContextDto();
        dto.receiverEmail = receiverEmail;
        dto.password = password;
        return dto;
    }

    public static EmailContextDto ofCreateAuthCodeInfo(String receiverEmail, String authCode) {
        EmailContextDto dto = new EmailContextDto();
        dto.receiverEmail = receiverEmail;
        dto.authCode = authCode;
        return dto;
    }
}
