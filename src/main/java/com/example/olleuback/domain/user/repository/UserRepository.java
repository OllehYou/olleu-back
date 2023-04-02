package com.example.olleuback.domain.user.repository;

import com.example.olleuback.domain.user.entity.User;
import java.util.Optional;
import java.util.OptionalDouble;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByEmail(String email);
    Boolean existsByNickname(String nickname);

    Optional<User> findByEmail(String email);
}
