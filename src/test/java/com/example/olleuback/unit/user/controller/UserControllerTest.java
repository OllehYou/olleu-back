package com.example.olleuback.unit.user.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.olleuback.domain.user.controller.UserController;
import com.example.olleuback.domain.user.dto.CreateUserDto;
import com.example.olleuback.domain.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
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
}
