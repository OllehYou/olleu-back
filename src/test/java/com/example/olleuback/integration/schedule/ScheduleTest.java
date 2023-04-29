package com.example.olleuback.integration.schedule;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;

import com.example.olleuback.config.restdocs.config.RestDocsConfig;
import com.example.olleuback.domain.schedule.entity.Schedule;
import com.example.olleuback.domain.schedule.repository.ScheduleRepository;
import com.example.olleuback.domain.user.entity.User;
import com.example.olleuback.domain.user.repository.UserRepository;
import java.time.LocalDateTime;
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
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
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

    final String SCHEDULE_URL = "/api/v1/schedules";

    @BeforeEach
    void setUp(final WebApplicationContext context, final RestDocumentationContextProvider provider) {
        this.mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
                .apply(springSecurity()).alwaysDo(print()).alwaysDo(restDocs)
                .addFilters(new CharacterEncodingFilter("UTF-8", true)).build();
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
                                fieldWithPath("id").description("유저 식별 번호"),
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

    protected Schedule saveSchedule(User user) {
        return scheduleRepository.save(
                Schedule.ofCreate(
                        "title", "locationName",
                        127.0, 42.0, LocalDateTime.now(),
                        "description", user)
        );
    }


    protected User saveUser() {
        return userRepository.save(
                User.ofSignup("email", "nickname", "password")
        );
    }
}
