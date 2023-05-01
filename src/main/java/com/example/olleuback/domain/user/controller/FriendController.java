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
    public ResponseEntity<Boolean> makeFriend(@PathVariable Long userId, @PathVariable Long friendUserId) {
        boolean result = friendService.makeFriend(userId, friendUserId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{userId}/friends/{friendUserId}/accept")
    public ResponseEntity<Object> acceptFriend(@PathVariable Long userId, @PathVariable Long friendUserId) {
        friendService.acceptFriend(userId, friendUserId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{userId}/friends/{friendUserId}/deny")
    public ResponseEntity<Object> denyFriend(@PathVariable Long userId, @PathVariable Long friendUserId) {
        friendService.denyFriend(userId, friendUserId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/friends/{friendUserId}")
    public ResponseEntity<Boolean> deleteFriend(@PathVariable Long userId, @PathVariable Long friendUserId) {
        friendService.deleteFriend(userId, friendUserId);
        return ResponseEntity.ok().build();
    }
}
