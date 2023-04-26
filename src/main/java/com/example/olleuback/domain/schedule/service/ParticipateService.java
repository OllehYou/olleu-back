package com.example.olleuback.domain.schedule.service;

import com.example.olleuback.common.exception.OlleUException;
import com.example.olleuback.domain.schedule.entity.Participate;
import com.example.olleuback.domain.schedule.repository.ParticipateRepository;
import com.example.olleuback.domain.schedule.entity.Schedule;
import com.example.olleuback.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParticipateService {
    private final ParticipateRepository participateRepository;

    public Participate createParticipate(Schedule schedule, User friend) {
        return participateRepository.save(Participate.ofNewInvite(friend, schedule));
    }

    @Transactional
    public void acceptInvitation(Long participateId, User user) {
        Participate participate = participateRepository.findByIdAndUser(participateId, user).orElseThrow(() -> {
            log.debug("ParticipateService.findById : Not Found Participate - id:{}", participateId);
            throw new OlleUException(404, "초대정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
        });
        participate.accept();
    }

    @Transactional
    public void denyInvitation(Long participateId, User user) {
        Participate participate = participateRepository.findByIdAndUser(participateId, user).orElseThrow(() -> {
            log.debug("ParticipateService.findById : Not Found Participate - id:{}", participateId);
            throw new OlleUException(404, "초대정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
        });
        participateRepository.delete(participate);
    }
}
