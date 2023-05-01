package com.example.olleuback.domain.user.controller;

import com.example.olleuback.domain.user.dto.FriendAcceptDto;
import com.example.olleuback.domain.user.dto.FriendDeleteDto;
import com.example.olleuback.domain.user.dto.FriendDenyDto;
import com.example.olleuback.domain.user.service.FriendService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class FriendController {
    private final FriendService friendService;

    @PostMapping("/{userId}/friends/{friendUserId}")
    public ResponseEntity<Boolean> follow(@PathVariable Long userId, @PathVariable Long friendUserId) {
        boolean result = friendService.makeFriend(userId, friendUserId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/friends/accept")
    public ResponseEntity<Object> acceptFriend(@RequestBody FriendAcceptDto friendAcceptDto) {
        friendService.acceptFriend(friendAcceptDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/friends/deny")
    public ResponseEntity<Object> denyFriend(@RequestBody FriendDenyDto friendDenyDto) {
        friendService.denyFriend(friendDenyDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/friends")
    public ResponseEntity<Boolean> deleteFriend(@RequestBody FriendDeleteDto friendDeleteDto) {
        friendService.deleteFriend(friendDeleteDto);
        return ResponseEntity.ok().build();
    }
}
