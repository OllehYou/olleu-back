package com.example.olleuback.domain.schedule.controller;

import com.example.olleuback.domain.schedule.service.ParticipateService;
import com.example.olleuback.domain.user.entity.User;
import com.example.olleuback.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/participates")
@RestController
@RequiredArgsConstructor
public class ParticipateController {
    private final ParticipateService participateService;
    private final UserService userService;
    @PostMapping("/{participateId}/accept/users/{userId}")
    public ResponseEntity<Object> acceptInvitationSchedule(@PathVariable Long participateId,
                                                           @PathVariable Long userId) {
        User user = userService.findById(userId);
        participateService.acceptInvitation(participateId, user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{participateId}/deny/users/{userId}")
    public ResponseEntity<Object> denyInvitationSchedule(@PathVariable Long participateId,
                                                           @PathVariable Long userId) {
        User user = userService.findById(userId);
        participateService.denyInvitation(participateId, user);
        return ResponseEntity.ok().build();
    }
}
