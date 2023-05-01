package com.example.olleuback.helper.mapper.schedule;

import com.example.olleuback.domain.schedule.dto.ScheduleDto;
import com.example.olleuback.domain.schedule.entity.Schedule;
import com.example.olleuback.domain.user.dto.UserDto;

public class ScheduleMapper {
    public static ScheduleDto convertEntityToDto(Schedule entity) {
        return new ScheduleDto(entity.getId(), entity.getTitle(),
                               UserDto.ofCreate(
                                    entity.getUser().getId(),
                                    entity.getUser().getEmail(),
                                    entity.getUser().getNickname()
                               ),
                               entity.getLocationName(),
                               entity.getLatitude(), entity.getLongitude(),
                               entity.getMeetingDate(), entity.getDescription()
        );
    }
}
