package com.example.olleuback.domain.schedule.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScheduleDto {
    private Long id;
    private String title;
    private String locationName;
    private Double latitude;
    private Double longitude;
    private LocalDateTime meetingDate;
    private String description;

}
