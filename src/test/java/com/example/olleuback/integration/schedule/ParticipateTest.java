package com.example.olleuback.integration.schedule;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.olleuback.config.restdocs.config.RestDocsConfig;
import com.example.olleuback.domain.schedule.entity.Participate;
import com.example.olleuback.domain.schedule.entity.Schedule;
import com.example.olleuback.domain.schedule.repository.ParticipateRepository;
import com.example.olleuback.domain.schedule.repository.ScheduleRepository;
import com.example.olleuback.domain.user.entity.User;
import com.example.olleuback.domain.user.repository.UserRepository;
import com.example.olleuback.integration.BaseTest;
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


public class ParticipateTest extends BaseTest {


    @Test
    @WithMockUser("user")
    @DisplayName("일정 초대 수락 통합 테스트")
    void acceptParticipate() throws Exception {
        //given
        User user = super.saveUser();
        User friend = super.saveUser();
        Schedule schedule = super.saveSchedule(user);
        Participate participate = super.saveParticipate(friend, schedule);

        //when
        ResultActions result =
                mvc.perform(post(PARTICIPATE_URL + "/{participateId}/accept/users/{userId}",
                                 participate.getId(), friend.getId())
                                                   .contentType(MediaType.APPLICATION_JSON));
        //then
        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("participateId").description("초대 정보 식별 번호"),
                                parameterWithName("userId").description("유저 식별 번호")
                        )));
    }

    @Test
    @WithMockUser("user")
    @DisplayName("일정 초대 거절 통합 테스트")
    void denyParticipate() throws Exception {
        //given
        User user = super.saveUser();
        User friend = super.saveUser();
        Schedule schedule = super.saveSchedule(user);
        Participate participate = super.saveParticipate(friend, schedule);

        //when
        ResultActions result =
                mvc.perform(post(PARTICIPATE_URL + "/{participateId}/deny/users/{userId}",
                                 participate.getId(), friend.getId())
                                    .contentType(MediaType.APPLICATION_JSON));
        //then
        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("participateId").description("초대 정보 식별 번호"),
                                parameterWithName("userId").description("유저 식별 번호")
                        )));
    }

}
