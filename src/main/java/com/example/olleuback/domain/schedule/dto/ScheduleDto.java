package com.example.olleuback.domain.schedule.dto;

import com.example.olleuback.domain.user.dto.UserDto;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDto {
    private Long id;
    private String title;
    private UserDto userDto;
    private String locationName;
    private Double latitude;
    private Double longitude;
    private LocalDateTime meetingDate;
    private String description;
}
