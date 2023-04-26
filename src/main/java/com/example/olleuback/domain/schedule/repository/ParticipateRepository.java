package com.example.olleuback.domain.schedule.repository;

import com.example.olleuback.domain.schedule.entity.Participate;
import com.example.olleuback.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipateRepository extends JpaRepository<Participate, Long> {
    Optional<Participate> findByIdAndUser(Long id, User user);
}
