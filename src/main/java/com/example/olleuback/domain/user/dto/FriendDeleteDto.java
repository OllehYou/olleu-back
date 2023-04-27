package com.example.olleuback.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FriendDeleteDto {
    private Long userId;
    private Long friendId;
}
