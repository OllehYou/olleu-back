package com.example.olleuback.integration;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.example.olleuback.config.restdocs.config.RestDocsConfig;
import com.example.olleuback.domain.schedule.entity.Participate;
import com.example.olleuback.domain.schedule.entity.Schedule;
import com.example.olleuback.domain.schedule.repository.ParticipateRepository;
import com.example.olleuback.domain.schedule.repository.ScheduleRepository;
import com.example.olleuback.domain.user.entity.User;
import com.example.olleuback.domain.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Random;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@ActiveProfiles("test")
@Import(RestDocsConfig.class)
@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
public class BaseTest {

    @Autowired
    protected MockMvc mvc;
    @Autowired
    protected RestDocumentationResultHandler restDocs;
    @Autowired
    protected ScheduleRepository scheduleRepository;
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected ParticipateRepository participateRepository;
    @Autowired
    protected ObjectMapper objectMapper;

    protected final String PARTICIPATE_URL = "/api/v1/participates";
    protected final String SCHEDULE_URL = "/api/v1/schedules";

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

    protected Participate saveParticipate(User user, Schedule schedule) {
        return participateRepository.save(
                Participate.ofNewInvite(user, schedule)
        );
    }

}
