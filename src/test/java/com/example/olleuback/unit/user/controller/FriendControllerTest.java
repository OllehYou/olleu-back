package com.example.olleuback.unit.user.controller;

import com.example.olleuback.domain.user.controller.FriendController;
import com.example.olleuback.domain.user.dto.FriendAcceptDto;
import com.example.olleuback.domain.user.dto.FriendDeleteDto;
import com.example.olleuback.domain.user.dto.FriendDenyDto;
import com.example.olleuback.domain.user.service.FriendService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = FriendController.class)
public class FriendControllerTest {
    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    FriendService friendService;

    @Test
    @DisplayName("친구 초대 컨트롤러 단위 테스트")
    void follow() throws Exception {
        //given
        Long userId = 1L;
        Long friendUserId = 2L;

        //when
        ResultActions result = mvc.perform(post("/api/v1/users/{userId}/friends/{friendUserId}", userId, friendUserId)
                .contentType("application/json;charset=UTF-8"));

        //then
        result.andExpect(status().isOk()).andDo(print());
    }

    @Test
    @DisplayName("친구 수락 컨트롤러 단위 테스트")
    void acceptFriend() throws Exception {
        //given
        FriendAcceptDto friendAcceptDto = new FriendAcceptDto(1L, 2L);

        //when
        ResultActions result = mvc.perform(post("/api/v1/users/friends/accept")
                .contentType("application/json;charset=UTF-8")
                .content(objectMapper.writeValueAsString(friendAcceptDto)));

        //then
        result.andExpect(status().isOk()).andDo(print());
    }

    @Test
    @DisplayName("친구 거절 컨트롤러 단위 테스트")
    void denyFriend() throws Exception {
        //given
        FriendDenyDto friendDenyDto = new FriendDenyDto(1L, 2L);

        //when
        ResultActions result = mvc.perform(post("/api/v1/users/friends/deny")
                .contentType("application/json;charset=UTF-8")
                .content(objectMapper.writeValueAsString(friendDenyDto)));

        //then
        result.andExpect(status().isOk()).andDo(print());
    }

    @Test
    @DisplayName("친구 삭제 컨트롤러 단위 테스트")
    void deleteFriend() throws Exception {
        //given
        FriendDeleteDto friendDeleteDto = new FriendDeleteDto(1L, 2L);

        //when
        ResultActions result = mvc.perform(delete("/api/v1/users/friends")
                .contentType("application/json;charset=UTF-8")
                .content(objectMapper.writeValueAsString(friendDeleteDto)));

        //then
        result.andExpect(status().isOk()).andDo(print());
    }
}
