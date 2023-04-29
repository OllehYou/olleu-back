package com.example.olleuback.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class FriendAcceptDto {
	private long myId;
	private long friendId;
}
