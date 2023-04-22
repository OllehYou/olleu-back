package com.example.olleuback.domain.user.dto;

import lombok.Data;
import lombok.Getter;

@Data
public class UserDto {
    private Long id;
    private String email;
    private String nickname;

    public static UserDto ofCreate(Long id, String email, String nickname) {
        UserDto userDto = new UserDto();
        userDto.id = id;
        userDto.email = email;
        userDto.nickname = nickname;
        return userDto;
    }
}
