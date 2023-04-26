package com.example.olleuback.unit.user.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.example.olleuback.domain.schedule.dto.SchedulesDto;
import com.example.olleuback.domain.schedule.entity.Participate;
import com.example.olleuback.domain.schedule.entity.Schedule;
import com.example.olleuback.domain.schedule.repository.ParticipateRepository;
import com.example.olleuback.domain.schedule.repository.ScheduleRepository;
import com.example.olleuback.domain.schedule.service.ParticipateService;
import com.example.olleuback.domain.schedule.service.ScheduleService;
import com.example.olleuback.domain.user.entity.User;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class ParticipateServiceTest {
    @InjectMocks
    ParticipateService participateService;
    @Mock
    ParticipateRepository participateRepository;
    @Test
    @DisplayName("일정 초대 수락 서비스 테스트")
    void accept() {
        //given
        User friend = User.ofSignup("email@naver.com", "nickname", "password");
        Schedule schedule = new Schedule();
        Participate participate = new Participate();
        given(participateRepository.save(any())).willReturn(participate);

        //when
        participateService.createParticipate(schedule, friend);

        //then
    }

    @Test
    @DisplayName("일정 초대 거절 서비스 테스트")
    void deny() {
        //given
        User userMock = mock(User.class);
        Participate participateMock =  mock(Participate.class);
        given(participateRepository.findByIdAndUser(anyLong(), any())).willReturn(Optional.of(participateMock));

        //when
        participateService.denyInvitation(1L, userMock);

        //then
        verify(participateRepository, times(1)).delete(any(Participate.class));
    }
}
