package com.example.olleuback.unit.user.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.olleuback.domain.user.controller.UserController;
import com.example.olleuback.domain.user.dto.CreateUserDto;
import com.example.olleuback.domain.user.dto.LoginUserDto;
import com.example.olleuback.domain.user.dto.LoginUserDto.Response;
import com.example.olleuback.domain.user.dto.UpdateUserInfoDto;
import com.example.olleuback.domain.user.dto.UserDto;
import com.example.olleuback.domain.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = UserController.class)
public class UserControllerTest{
    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    UserService userService;

    @Test
    @DisplayName("회원가입 컨트롤러 단위 테스트")
    void signup() throws Exception {
        //given
        CreateUserDto createUserDto = new CreateUserDto();
        createUserDto.setEmail("email@gmail.com");
        createUserDto.setNickname("nickname");
        createUserDto.setPassword("password");

        //when
        ResultActions result = mvc.perform(post("/api/v1/users/signup")
            .contentType("application/json;charset=UTF-8")
            .content(objectMapper.writeValueAsString(createUserDto)));

        //then
        result.andExpect(status().isOk()).andDo(print());
    }

    @Test
    @DisplayName("로그인 컨트롤러 단위 테스트")
    void login() throws Exception {
        //given
        LoginUserDto.Request loginUserRequest = new LoginUserDto.Request();
        loginUserRequest.setEmail("email@gmail.com");
        loginUserRequest.setPassword("password");

        LoginUserDto.Response loginUserResponse = new LoginUserDto.Response();
        loginUserResponse.setId(1L);

        given(userService.login(loginUserRequest)).willReturn(loginUserResponse);

        //when
        ResultActions result = mvc.perform(post("/api/v1/users/login")
            .contentType("application/json;charset=UTF-8")
            .content(objectMapper.writeValueAsString(loginUserRequest)));

        //then
        result.andExpect(status().isOk()).andDo(print());
    }

    @Test
    @DisplayName("유저 정보 업데이트 컨트롤러 단위 테스트")
    void updateUserInfo() throws Exception {
        //given
        UpdateUserInfoDto updateUserInfoDto = new UpdateUserInfoDto();
        updateUserInfoDto.setId(1L);
        updateUserInfoDto.setNickname("updateNickname");

        given(userService.updateUserInfo(updateUserInfoDto)).willReturn(true);

        //when
        ResultActions result = mvc.perform(patch("/api/v1/users/info")
            .contentType("application/json;charset=UTF-8")
            .content(objectMapper.writeValueAsString(updateUserInfoDto)));
    }

    @Test
    @DisplayName("유저 정보 조회 컨트롤러 단위 테스트")
    void getUserInfo() throws Exception {
        //given
        UserDto userDto = UserDto.ofCreate(1L, "email@gmail.com", "nickname");

        given(userService.getUserInfo(userDto.getId())).willReturn(userDto);

        //when
        ResultActions result = mvc.perform(get("/api/v1/users/{userId}", userDto.getId())
            .contentType("application/json;charset=UTF-8"));

        //then
        result.andExpect(status().isOk()).andDo(print());
    }

    @Test
    @DisplayName("토큰 갱신 컨트롤러 단위 테스트")
    void refresh() throws Exception {
        //given
        String refreshToken = "refreshToken";

        //when
        ResultActions result = mvc.perform(post("/api/v1/users/refresh")
            .contentType("application/json;charset=UTF-8")
            .content(objectMapper.writeValueAsString(refreshToken)));

        //then
        result.andExpect(status().isOk()).andDo(print());
    }
}
