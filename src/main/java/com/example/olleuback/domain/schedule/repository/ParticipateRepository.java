package com.example.olleuback.domain.schedule.repository;

import com.example.olleuback.domain.schedule.entity.Participate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipateRepository extends JpaRepository<Participate, Long> {

}
