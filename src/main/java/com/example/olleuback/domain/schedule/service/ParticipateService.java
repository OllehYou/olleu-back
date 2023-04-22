package com.example.olleuback.domain.schedule.service;

import com.example.olleuback.domain.schedule.entity.Participate;
import com.example.olleuback.domain.schedule.repository.ParticipateRepository;
import com.example.olleuback.domain.schedule.entity.Schedule;
import com.example.olleuback.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParticipateService {
    private final ParticipateRepository participateRepository;

    public Participate createParticipate(Schedule schedule, User friend) {
        return participateRepository.save(Participate.ofNewInvite(friend, schedule));
    }
}
