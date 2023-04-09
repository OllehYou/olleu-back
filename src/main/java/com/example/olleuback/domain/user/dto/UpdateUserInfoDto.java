package com.example.olleuback.domain.user.dto;

import lombok.Data;

@Data
public class UpdateUserInfoDto {
    private Long id;
    private String nickname;

    //TODO 프로필 기능 추가
}
