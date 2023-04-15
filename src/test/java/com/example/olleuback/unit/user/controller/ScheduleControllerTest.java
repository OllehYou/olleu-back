package com.example.olleuback.unit.user.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.olleuback.domain.schedule.controller.ScheduleController;
import com.example.olleuback.domain.schedule.dto.SchedulesDto;
import com.example.olleuback.domain.schedule.service.ScheduleService;
import com.example.olleuback.domain.user.controller.UserController;
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
@WebMvcTest(controllers = ScheduleController.class)
public class ScheduleControllerTest {
    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    UserService userService;
    @MockBean
    ScheduleService scheduleService;

    @Test
    @DisplayName("스케쥴 조회 테스트")
    void test() throws Exception {
        //given
        User user = User.ofSignup("email@naver.com", "nickname", "password");
        Pageable pageable = PageRequest.of(1, 10);
        SchedulesDto schedulesDto = SchedulesDto.builder().scheduleDtos(new ArrayList<>())
                .page(Paging.builder().build()).build();
        given(userService.findById(1L)).willReturn(user);
        given(scheduleService.findAllScheduleByUserId(user, pageable)).willReturn(schedulesDto);

        //when
        ResultActions result = mvc.perform(get("/api/v1/schedules/users/{userId}", 1)
                                                   .contentType("application/json;charset=UTF-8")
                                                   .param("pageNumber", "1")
                                                   .param("pageSize", "5"));
        //then
        result.andExpect(status().isOk()).andDo(print());
    }
}
