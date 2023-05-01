package com.example.olleuback.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.olleuback.domain.user.entity.Following;
import com.example.olleuback.domain.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface FollowingRepository extends JpaRepository<Following, Long> {

    boolean existsByUserIdAndFollowingUserId(Long userId, Long followingUserId);

    Optional<Following> findByUserAndFollowingUser(User friend, User user);

    List<Following> findByUser(User user);
}
