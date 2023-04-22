package com.example.olleuback.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.olleuback.domain.user.entity.Following;

public interface FollowingRepository extends JpaRepository<Following, Long> {
	boolean existsByUserIdAndFollowingUserId(Long userId, Long followingUserId);
}
