package com.example.olleuback.integration.schedule;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.olleuback.config.restdocs.config.RestDocsConfig;
import com.example.olleuback.domain.schedule.entity.Schedule;
import com.example.olleuback.domain.schedule.repository.ParticipateRepository;
import com.example.olleuback.domain.schedule.repository.ScheduleRepository;
import com.example.olleuback.domain.user.entity.User;
import com.example.olleuback.domain.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Random;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@ActiveProfiles("test")
@Import(RestDocsConfig.class)
@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
public class ScheduleTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    RestDocumentationResultHandler restDocs;
    @Autowired
    ScheduleRepository scheduleRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ParticipateRepository participateRepository;

    final String SCHEDULE_URL = "/api/v1/schedules";

    @BeforeEach
    void setUp(final WebApplicationContext context, final RestDocumentationContextProvider provider) {
        this.mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
                .apply(springSecurity()).alwaysDo(print()).alwaysDo(restDocs)
                .addFilters(new CharacterEncodingFilter("UTF-8", true)).build();
    }

    @AfterEach
    void dbClear() {
        participateRepository.deleteAll();
        scheduleRepository.deleteAll();
        userRepository.deleteAll();
    }

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
    protected Schedule saveSchedule(User user) {
        return scheduleRepository.save(
                Schedule.ofCreate(
                        "title", "locationName",
                        127.0, 42.0, LocalDateTime.now(),
                        "description", user)
        );
    }


    protected User saveUser() {
        Random random = new Random();
        return userRepository.save(
                User.ofSignup("email"+ random.nextInt(),
                              "nickname" + random.nextInt(),
                              "password")
        );
    }
}
