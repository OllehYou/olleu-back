package com.example.olleuback.domain.user.controller;

import com.example.olleuback.domain.user.dto.ChangePasswordDto;
import com.example.olleuback.domain.user.dto.AuthCodeConfirmDto;
import com.example.olleuback.domain.user.dto.CreateUserDto;
import com.example.olleuback.domain.user.dto.LoginUserDto;
import com.example.olleuback.domain.user.dto.AuthCodeDto;
import com.example.olleuback.domain.user.dto.UpdateUserInfoDto;
import com.example.olleuback.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import com.example.olleuback.domain.user.dto.UserDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@RequestBody CreateUserDto createUserDto){
        userService.signup(createUserDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginUserDto.Response> login(@RequestBody LoginUserDto.Request loginUserRequest) {
        LoginUserDto.Response loginUserResponse = userService.login(loginUserRequest);
        return ResponseEntity.ok(loginUserResponse);
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

    @PatchMapping("/info")
    public ResponseEntity<Boolean> updateUserInfo(@RequestBody UpdateUserInfoDto updateUserInfoDto){
        boolean result = userService.updateUserInfo(updateUserInfoDto);
        return ResponseEntity.ok(result);

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserInfo(@PathVariable Long userId) {
        UserDto userDto = userService.getUserInfo(userId);
        return ResponseEntity.ok(userDto);

    }
}
