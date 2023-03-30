package com.example.olleuback.domain.participate.entity;

import com.example.olleuback.common.OlleUEnum.ParticipateStatus;
import com.example.olleuback.domain.post.entity.Schedule;
import com.example.olleuback.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Getter
@Entity
@EnableJpaAuditing
@NoArgsConstructor
public class Participate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "schedule_id", referencedColumnName = "id")
    private Schedule schedule;
    @Column(name = "status")
    private ParticipateStatus status;

}