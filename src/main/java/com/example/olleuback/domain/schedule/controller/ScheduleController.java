package com.example.olleuback.domain.schedule.controller;

import com.example.olleuback.domain.schedule.dto.SchedulesDto;
import com.example.olleuback.domain.schedule.service.ScheduleService;
import com.example.olleuback.domain.user.entity.User;
import com.example.olleuback.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/schedules")
@RestController
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;
    private final UserService userService;

    @GetMapping("/users/{userId}")
    public ResponseEntity<SchedulesDto> getSchedulesByUser(@PathVariable Long userId,
                                                           @RequestParam int pageNumber,
                                                           @RequestParam int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        User user = userService.findById(userId);
        SchedulesDto schedulesDto = scheduleService.findAllScheduleByUserId(user, pageable);
        return ResponseEntity.ok(schedulesDto);
    }
}
