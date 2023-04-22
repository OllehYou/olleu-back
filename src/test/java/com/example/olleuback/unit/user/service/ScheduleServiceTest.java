package com.example.olleuback.unit.user.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.example.olleuback.domain.schedule.dto.SchedulesDto;
import com.example.olleuback.domain.schedule.entity.Participate;
import com.example.olleuback.domain.schedule.entity.Schedule;
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
public class ScheduleServiceTest {
    @InjectMocks
    ScheduleService scheduleService;
    @Mock
    ScheduleRepository scheduleRepository;
    @Mock
    ParticipateService participateService;

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
    }
}
