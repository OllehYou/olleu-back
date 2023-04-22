package com.example.olleuback.domain.schedule.service;

import com.example.olleuback.common.exception.OlleUException;
import com.example.olleuback.domain.schedule.dto.ScheduleDto;
import com.example.olleuback.domain.schedule.dto.SchedulesDto;
import com.example.olleuback.domain.schedule.entity.Participate;
import com.example.olleuback.domain.schedule.entity.Schedule;
import com.example.olleuback.domain.schedule.repository.ScheduleRepository;
import com.example.olleuback.domain.user.entity.User;
import com.example.olleuback.helper.mapper.page.PagingMapper;
import com.example.olleuback.helper.mapper.schedule.ScheduleMapper;
import com.example.olleuback.utils.dto.page.Paging;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final ParticipateService participateService;

    @Transactional(readOnly = true)
    public SchedulesDto findAllScheduleByUserId(User user, Pageable pageable) {
        Page<Schedule> schedulePage = scheduleRepository.findAllByUserOrderByMeetingDateDesc(user, pageable);
        List<ScheduleDto> scheduleDtos = schedulePage.getContent().stream()
                .map(ScheduleMapper::convertEntityToDto).toList();

        Paging paging = PagingMapper.makePaging(pageable.getPageNumber(), pageable.getPageSize(), schedulePage.getTotalPages(), schedulePage.getTotalElements());
        return SchedulesDto.builder()
                .scheduleDtos(scheduleDtos)
                .page(paging)
                .build();
    }

    @Transactional
    public void inviteFriendToSchedule(Long scheduleId, User friend) {
        Schedule schedule = this.findById(scheduleId);
        Participate participate = participateService.createParticipate(schedule, friend);
        schedule.addParticipate(participate);
    }

    private Schedule findById(Long id) {
        return scheduleRepository.findById(id).orElseThrow(() -> {
            log.debug("ScheduleService.findById : Not Found Schedule - id:{}", id);
            throw new OlleUException(404, "일정을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
        });
    }
}
