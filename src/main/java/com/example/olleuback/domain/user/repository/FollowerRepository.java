package com.example.olleuback.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.olleuback.domain.user.entity.Follower;

@Repository
public interface FollowerRepository extends JpaRepository<Follower, Long> {
	boolean existsByUserIdAndFollowerUserId(Long userId, Long followingUserId);
}
