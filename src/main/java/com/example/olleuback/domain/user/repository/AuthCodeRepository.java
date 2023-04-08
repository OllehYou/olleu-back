package com.example.olleuback.domain.user.repository;

import com.example.olleuback.domain.user.entity.AuthCode;
import java.util.Optional;
import javax.swing.text.html.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthCodeRepository extends JpaRepository<AuthCode, Long> {
    Optional<AuthCode> findByUserId(Long userId);
}
