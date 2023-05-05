package com.example.olleuback.integration.schedule;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.olleuback.domain.schedule.dto.CreateScheduleDto;
import com.example.olleuback.domain.schedule.entity.Schedule;
import com.example.olleuback.domain.user.entity.User;
import com.example.olleuback.integration.BaseTest;
import java.time.LocalDateTime;
import java.util.Random;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

public class ScheduleTest extends BaseTest {

    @Test
    @WithMockUser(username = "user")
    @DisplayName("일정 조회 통합 테스트")
    void getScheduleTest() throws Exception {
        //given
        User user = this.saveUser();
        Schedule schedule = this.saveSchedule(user);

        //when
        ResultActions result = mvc.perform(get(SCHEDULE_URL + "/{scheduleId}", schedule.getId()).contentType(
                        MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("scheduleId").description("일정 아이디 번호")
                        ),
                        responseFields(
                                fieldWithPath("id").description("일정 식별 번호"),
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("userDto.id").description("유저 식별 번호"),
                                fieldWithPath("userDto.email").description("유저 이메일"),
                                fieldWithPath("userDto.nickname").description("유저 닉네임"),
                                fieldWithPath("locationName").description("장소 이름"),
                                fieldWithPath("latitude").description("위도"),
                                fieldWithPath("longitude").description("경도"),
                                fieldWithPath("meetingDate").description("모임 일자"),
                                fieldWithPath("description").description("설명")
                        )));
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("일정 리스트 조회 통합 테스트")
    void getScheduleByUserTest() throws Exception {
        //given
        User user = this.saveUser();
        this.saveSchedule(user);
        this.saveSchedule(user);

        //when
        ResultActions result = mvc.perform(get(SCHEDULE_URL + "/users/{userId}", user.getId())
                                                   .contentType(MediaType.APPLICATION_JSON)
                                                   .param("pageNumber", "1")
                                                   .param("pageSize", "2"));

        //then
        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("userId").description("유저 식별 번호")
                        ),
                        queryParameters(
                                parameterWithName("pageNumber").description("페이지 번호"),
                                parameterWithName("pageSize").description("페이지 당 일정 수")
                        ),
                        responseFields(
                                fieldWithPath("scheduleDtos[].id").description("일정 식별 번호"),
                                fieldWithPath("scheduleDtos[].title").description("제목"),
                                fieldWithPath("scheduleDtos[].userDto.id").description("유저 식별 번호"),
                                fieldWithPath("scheduleDtos[].userDto.email").description("유저 이메일"),
                                fieldWithPath("scheduleDtos[].userDto.nickname").description("유저 닉네임"),
                                fieldWithPath("scheduleDtos[].locationName").description("장소 이름"),
                                fieldWithPath("scheduleDtos[].latitude").description("위도"),
                                fieldWithPath("scheduleDtos[].longitude").description("경도"),
                                fieldWithPath("scheduleDtos[].meetingDate").description("모임 일자"),
                                fieldWithPath("scheduleDtos[].description").description("설명"),
                                fieldWithPath("page.pageSize").description("페이지 크기"),
                                fieldWithPath("page.pageNumber").description("페이지 번호"),
                                fieldWithPath("page.totalElements").description("총 일정 수"),
                                fieldWithPath("page.totalPage").description("페이지 수")
                        )));
    }

    @Test
    @WithMockUser(username = "user")
    @DisplayName("일정 초대 통합 테스트")
    void inviteFriend() throws Exception {
        //given
        User user = this.saveUser();
        Schedule schedule = this.saveSchedule(user);
        User friend = this.saveUser();

        //when
        ResultActions result =
                mvc.perform(post(SCHEDULE_URL + "/{scheduleId}/invite/friends/{friendId}", schedule.getId(), friend.getId())
                                                   .contentType(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("scheduleId").description("일정 아이디 번호"),
                                parameterWithName("friendId").description("친구 유저 아이디 번호")
                        )));
    }

    @Test
    @WithMockUser
    @DisplayName("일정 생성 통합 테스트")
    void createScheduleTest() throws Exception {
        User user = super.saveUser();
        CreateScheduleDto input = new CreateScheduleDto();
        input.setTitle("title");
        input.setLocationName("location name");
        input.setLatitude(1.0);
        input.setLongitude(4.0);
        input.setMeetingDate(LocalDateTime.now());
        input.setDescription("description");

        ResultActions result = mvc.perform(post(SCHEDULE_URL + "/users/{userId}", user.getId())
                .contentType("application/json;utf-8")
                .content(objectMapper.writeValueAsString(input)));

        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        pathParameters(
                          parameterWithName("userId").description("유저 식별 번호")
                        ),
                        requestFields(
                                fieldWithPath("title").description("제목"),
                                fieldWithPath("locationName").description("장소 이름"),
                                fieldWithPath("latitude").description("위도"),
                                fieldWithPath("longitude").description("경도"),
                                fieldWithPath("meetingDate").description("약속일자"),
                                fieldWithPath("description").description("설명")
                        )
                ));
    }
}
