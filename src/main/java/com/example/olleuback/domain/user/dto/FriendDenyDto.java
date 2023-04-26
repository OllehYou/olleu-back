package com.example.olleuback.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FriendDenyDto {
	private long myId;
	private long friendId;
}
