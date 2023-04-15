package com.example.olleuback.helper.mapper.schedule;

import com.example.olleuback.domain.schedule.dto.ScheduleDto;
import com.example.olleuback.domain.schedule.entity.Schedule;

public class ScheduleMapper {
    public static ScheduleDto convertEntityToDto(Schedule entity) {
        return ScheduleDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .locationName(entity.getLocationName())
                .longitude(entity.getLongitude())
                .latitude(entity.getLatitude())
                .meetingDate(entity.getMeetingDate())
                .description(entity.getDescription())
                .build();
    }
}
