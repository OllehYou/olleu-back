package com.example.olleuback.unit.user.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.example.olleuback.domain.schedule.dto.SchedulesDto;
import com.example.olleuback.domain.schedule.entity.Schedule;
import com.example.olleuback.domain.schedule.repository.ScheduleRepository;
import com.example.olleuback.domain.schedule.service.ScheduleService;
import com.example.olleuback.domain.user.entity.User;
import java.util.List;
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
}
