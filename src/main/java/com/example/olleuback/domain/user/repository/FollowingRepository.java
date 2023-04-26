package com.example.olleuback.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.olleuback.domain.user.entity.Following;
import com.example.olleuback.domain.user.entity.User;

public interface FollowingRepository extends JpaRepository<Following, Long> {
	boolean existsByUserIdAndFollowingUserId(Long userId, Long followingUserId);

	Optional<Following> findByUserAndFollowingUser(User user, User followingUser);
}
