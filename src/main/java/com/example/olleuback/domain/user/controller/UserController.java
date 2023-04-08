package com.example.olleuback.domain.user.controller;

import com.example.olleuback.common.security.JwtProvider;
import com.example.olleuback.domain.user.dto.CreateUserDto;
import com.example.olleuback.domain.user.dto.LoginUserDto;
import com.example.olleuback.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
