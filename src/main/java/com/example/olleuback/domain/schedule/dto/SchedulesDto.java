package com.example.olleuback.domain.schedule.dto;

import com.example.olleuback.utils.dto.page.Paging;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SchedulesDto {
    private List<ScheduleDto> scheduleDtos;
    private Paging page;
}
