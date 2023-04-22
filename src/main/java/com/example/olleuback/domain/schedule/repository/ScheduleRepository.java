package com.example.olleuback.domain.schedule.repository;

import com.example.olleuback.domain.schedule.entity.Schedule;
import com.example.olleuback.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    Page<Schedule> findAllByUserOrderByMeetingDateDesc(User user, Pageable pageable);
}
