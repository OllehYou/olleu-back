package com.example.olleuback.unit.user.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.olleuback.domain.schedule.controller.ParticipateController;
import com.example.olleuback.domain.schedule.controller.ScheduleController;
import com.example.olleuback.domain.schedule.dto.SchedulesDto;
import com.example.olleuback.domain.schedule.service.ParticipateService;
import com.example.olleuback.domain.schedule.service.ScheduleService;
import com.example.olleuback.domain.user.entity.User;
import com.example.olleuback.domain.user.service.UserService;
import com.example.olleuback.utils.dto.page.Paging;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = ParticipateController.class)
public class ParticipateControllerTest {
    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    UserService userService;
    @MockBean
    ParticipateService participateService;

    @Test
    @DisplayName("일정 초대 컨트롤러  테스트")
    void accept() throws Exception {
        //given
        User userMock = mock(User.class);

        given(userService.findById(1L)).willReturn(userMock);

        //when
        ResultActions result = mvc.perform(post("/api/v1/participates/{participateId}/accept/users/{userId}", 1, 1)
                                                   .contentType("application/json;charset=UTF-8"));
        //then
        result.andExpect(status().isOk()).andDo(print());
        verify(participateService, times(1)).acceptInvitation(anyLong(), any(User.class));
    }

    @Test
    @DisplayName("일정 초대 컨트롤러  테스트")
    void deny() throws Exception {
        //given
        User userMock = mock(User.class);

        given(userService.findById(1L)).willReturn(userMock);

        //when
        ResultActions result = mvc.perform(post("/api/v1/participates/{participateId}/deny/users/{userId}", 1, 1)
                                                   .contentType("application/json;charset=UTF-8"));
        //then
        result.andExpect(status().isOk()).andDo(print());
        verify(participateService, times(1)).denyInvitation(anyLong(), any(User.class));
    }
}
