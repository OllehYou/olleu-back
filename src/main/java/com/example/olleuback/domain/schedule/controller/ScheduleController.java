package com.example.olleuback.domain.schedule.controller;

import com.example.olleuback.domain.schedule.dto.CreateScheduleDto;
import com.example.olleuback.domain.schedule.dto.ScheduleDto;
import com.example.olleuback.domain.schedule.dto.SchedulesDto;
import com.example.olleuback.domain.schedule.service.ScheduleService;
import com.example.olleuback.domain.user.entity.User;
import com.example.olleuback.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        Pageable pageable = PageRequest.of(pageNumber-1, pageSize);
        User user = userService.findById(userId);
        SchedulesDto schedulesDto = scheduleService.findAllScheduleByUserId(user, pageable);
        return ResponseEntity.ok(schedulesDto);
    }

    @PostMapping("/{scheduleId}/invite/friends/{friendId}")
    public ResponseEntity<Object> inviteFriendToSchedule(@PathVariable Long scheduleId,
                                                         @PathVariable Long friendId) {
        User friend = userService.findById(friendId);
        scheduleService.inviteFriendToSchedule(scheduleId, friend);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleDto> getSchedule(@PathVariable Long scheduleId) {
        return ResponseEntity.ok(scheduleService.getSchedule(scheduleId));
    }

    @PostMapping("/users/{userId}")
    public ResponseEntity<Boolean> createSchedule(@PathVariable Long userId,
                                                  @Valid @RequestBody CreateScheduleDto createScheduleDto) {
        User user = userService.findById(userId);
        return ResponseEntity.ok(scheduleService.createSchedule(user, createScheduleDto));
    }
}
