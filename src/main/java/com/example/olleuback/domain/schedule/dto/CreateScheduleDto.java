package com.example.olleuback.domain.schedule.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateScheduleDto {

    @NotNull
    private String title;
    private String locationName;
    private Double latitude;
    private Double longitude;
    @NotNull
    private LocalDateTime meetingDate;
    private String description;
}
