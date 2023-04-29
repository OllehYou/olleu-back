package com.example.olleuback.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.olleuback.domain.user.entity.Follower;
import com.example.olleuback.domain.user.entity.User;

@Repository
public interface FollowerRepository extends JpaRepository<Follower, Long> {

    boolean existsByUserIdAndFollowerUserId(Long userId, Long followingUserId);

    Optional<Follower> findByUserAndFollowerUser(User user, User friend);
}
