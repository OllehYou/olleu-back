package com.example.olleuback.domain.user.repository;

import com.example.olleuback.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.olleuback.domain.user.entity.Follower;

import java.util.Optional;

@Repository
public interface FollowerRepository extends JpaRepository<Follower, Long> {
	boolean existsByUserIdAndFollowerUserId(Long userId, Long followingUserId);

    Optional<Follower> findByUserAndFollowerUser(User user, User friend);
}
