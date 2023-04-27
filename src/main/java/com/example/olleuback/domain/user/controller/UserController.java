package com.example.olleuback.domain.user.controller;

import com.example.olleuback.common.security.JwtProvider;

import com.example.olleuback.domain.user.dto.*;

import com.example.olleuback.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtProvider jwtProvider;

    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@RequestBody CreateUserDto createUserDto) {
        userService.signup(createUserDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginUserDto.Response> login(@RequestBody LoginUserDto.Request loginUserRequest) {
        LoginUserDto.Response loginUserResponse = userService.login(loginUserRequest);
        return ResponseEntity.ok(loginUserResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<Object> refreshToken(@RequestBody String refreshToken) {
        String accessToken = jwtProvider.generateAccessTokenWithRefreshToken(refreshToken);
        return ResponseEntity.ok(accessToken);
    }

    @PatchMapping("/change/password")
    public ResponseEntity<Boolean> changePassword(@RequestBody ChangePasswordDto changePasswordDto) {
        userService.changePassword(changePasswordDto.getId(), changePasswordDto.getNewPassword());
        return ResponseEntity.ok(true);
    }
    @PostMapping("/send/authCode")
    public ResponseEntity<Boolean> sendAuthCode(@RequestBody AuthCodeDto authCodeDto) {
        userService.requestAuthCode(authCodeDto.getId());
        return ResponseEntity.ok(true);
    }

    @PostMapping("/confirm/authCode")
    public ResponseEntity<Boolean> confirmAuthCode(@RequestBody AuthCodeConfirmDto authCodeConfirmDto) {
        userService.confirmAuthCode(authCodeConfirmDto.getId(), authCodeConfirmDto.getAuthCode());
        return ResponseEntity.ok(true);
    }

    @PatchMapping("/info")
    public ResponseEntity<Boolean> updateUserInfo(@RequestBody UpdateUserInfoDto updateUserInfoDto) {
        boolean result = userService.updateUserInfo(updateUserInfoDto);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserInfo(@PathVariable Long userId) {
        UserDto userDto = userService.getUserInfo(userId);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/{userId}/follow/{followingUserId}")
    public ResponseEntity<Boolean> follow(@PathVariable Long userId, @PathVariable Long followingUserId) {
        boolean result = userService.follow(userId, followingUserId);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/friends")
    public ResponseEntity<Boolean> deleteFriend(@RequestBody FriendDeleteDto friendDeleteDto) {
        userService.deleteFriend(friendDeleteDto);
        return ResponseEntity.ok().build();
    }
}
