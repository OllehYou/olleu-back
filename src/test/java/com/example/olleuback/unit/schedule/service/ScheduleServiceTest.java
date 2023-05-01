package com.example.olleuback.unit.schedule.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.util.AssertionErrors.assertEquals;

import com.example.olleuback.domain.schedule.dto.ScheduleDto;
import com.example.olleuback.domain.schedule.dto.SchedulesDto;
import com.example.olleuback.domain.schedule.entity.Participate;
import com.example.olleuback.domain.schedule.entity.Schedule;
import com.example.olleuback.domain.schedule.repository.ScheduleRepository;
import com.example.olleuback.domain.schedule.service.ParticipateService;
import com.example.olleuback.domain.schedule.service.ScheduleService;
import com.example.olleuback.domain.user.entity.User;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class ScheduleServiceTest {
    @InjectMocks
    ScheduleService scheduleService;
    @Mock
    ScheduleRepository scheduleRepository;
    @Mock
    ParticipateService participateService;
    Logger log = LoggerFactory.getLogger(Logger.class);

    @Test
    @DisplayName("스케쥴 조회 테스트")
    void test() {
        //given
        User user = User.ofSignup("email@naver.com", "nickname", "password");
        Pageable pageable = PageRequest.of(1, 10);
        Page<Schedule> page = new PageImpl<>(List.of(new Schedule()), pageable, 1L);
        given(scheduleRepository.findAllByUserOrderByMeetingDateDesc(user, pageable)).willReturn(page);

        //when
        SchedulesDto schedulesDto = scheduleService.findAllScheduleByUserId(user, pageable);

        //then
        assertThat(schedulesDto.getScheduleDtos().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("일정 초대 서비스 테스트")
    void invite() {
        //given
        User friend = User.ofSignup("email@naver.com", "nickname", "password");
        Schedule schedule = new Schedule();
        Participate participate = new Participate();
        given(scheduleRepository.findById(any())).willReturn(Optional.of(schedule));
        given(participateService.createParticipate(schedule, friend)).willReturn(participate);

        //when
        scheduleService.inviteFriendToSchedule(1L, friend);

        //then
        verify(participateService, times(1)).createParticipate(any(Schedule.class), any(User.class));
    }

    @Test
    @DisplayName("일정 조회 서비스 테스트")
    void getSchedule() {
        //given
        Schedule scheduleMock = createScheduleMockAndSetStub();

        given(scheduleRepository.findById(anyLong())).willReturn(Optional.of(scheduleMock));

        //when
        ScheduleDto result = scheduleService.getSchedule(1L);

    }

    private static Schedule createScheduleMockAndSetStub() {
        Schedule scheduleMock = mock(Schedule.class);
        User userMock = mock(User.class);
        doReturn(1L).when(scheduleMock).getId();
        doReturn("title").when(scheduleMock).getTitle();
        doReturn("locationName").when(scheduleMock).getLocationName();
        doReturn(127.02).when(scheduleMock).getLongitude();
        doReturn(23.0).when(scheduleMock).getLatitude();
        doReturn(LocalDateTime.now()).when(scheduleMock).getMeetingDate();
        doReturn("description").when(scheduleMock).getDescription();
        doReturn(userMock).when(scheduleMock).getUser();
        doReturn(1L).when(userMock).getId();
        doReturn("userEmail").when(userMock).getEmail();
        doReturn("nickname").when(userMock).getNickname();
        return scheduleMock;
    }
}
