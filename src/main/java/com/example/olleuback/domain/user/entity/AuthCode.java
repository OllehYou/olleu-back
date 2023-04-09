package com.example.olleuback.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class AuthCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "authCode", nullable = false)
    private String authCode;
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    public static AuthCode ofCreate(Long userId, String authCode) {
        AuthCode entity = new AuthCode();
        entity.authCode = authCode;
        entity.userId = userId;
        return entity;
    }
}
