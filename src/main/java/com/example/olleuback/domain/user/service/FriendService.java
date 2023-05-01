package com.example.olleuback.domain.user.service;

import com.example.olleuback.common.exception.OlleUException;
import com.example.olleuback.common.olleu_enum.OlleUEnum;
import com.example.olleuback.domain.user.dto.UserDto;
import com.example.olleuback.domain.user.entity.Follower;
import com.example.olleuback.domain.user.entity.Following;
import com.example.olleuback.domain.user.entity.User;
import com.example.olleuback.domain.user.repository.FollowerRepository;
import com.example.olleuback.domain.user.repository.FollowingRepository;
import com.example.olleuback.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FriendService {
    private final UserRepository userRepository;
    private final FollowingRepository followingRepository;
    private final FollowerRepository followerRepository;

    @Transactional(readOnly = true)
    public List<UserDto> getFriends(Long userId) {
        User user = this.findById(userId);

        List<Following> followings = followingRepository.findByUser(user);
        List<Follower> followers = followerRepository.findByUser(user);

        List<UserDto> friends = new ArrayList<>();

        followings.forEach(following -> {
            if (following.getStatus().equals(OlleUEnum.FriendStatus.FRIEND)) {
                friends.add(UserDto.ofCreate(following.getFollowingUser().getId(), following.getFollowingUser().getEmail(), following.getFollowingUser().getNickname()));
            }
        });

        followers.forEach(follower -> {
            if (follower.getStatus().equals(OlleUEnum.FriendStatus.FRIEND)) {
                friends.add(UserDto.ofCreate(follower.getFollowerUser().getId(), follower.getFollowerUser().getEmail(), follower.getFollowerUser().getNickname()));
            }
        });

        return friends;
    }

    @Transactional(readOnly = true)
    public UserDto getFriend(Long userId, Long friendUserId) {
        User user = this.findById(userId);
        User friend = this.findById(friendUserId);

        Optional<Following> following = followingRepository.findByUserAndFollowingUser(user, friend);
        Optional<Follower> follower = followerRepository.findByUserAndFollowerUser(friend, user);

        if (!areFriends(following, follower)) {
            following = followingRepository.findByUserAndFollowingUser(friend, user);
            follower = followerRepository.findByUserAndFollowerUser(user, friend);
        }

        if (!areFriends(following, follower)) {
            throw new OlleUException(400, "친구가 아닙니다.", HttpStatus.BAD_REQUEST);
        }

        return UserDto.ofCreate(friend.getId(), friend.getEmail(), friend.getNickname());
    }

    @Transactional
    public boolean makeFriend(Long userId, Long friendUserId) {
        User user = this.findById(userId);
        User followingUser = this.findById(friendUserId);

        if (followingRepository.existsByUserIdAndFollowingUserId(userId, friendUserId) ||
                followerRepository.existsByUserIdAndFollowerUserId(friendUserId, userId)) {
            throw new OlleUException(400, "이미 팔로우한 유저입니다.", HttpStatus.BAD_REQUEST);
        }

        Following following = Following.ofCreate(user, followingUser);
        followingRepository.save(following);
        Follower follower = Follower.ofCreate(followingUser, user);
        followerRepository.save(follower);

        //TODO 친구 초대 푸시 알림

        return true;
    }

    @Transactional
    public void acceptFriend(Long userId, Long friendUserId) {
        User user = this.findById(userId);
        User friend = this.findById(friendUserId);

        Following following = followingRepository.findByUserAndFollowingUser(friend, user)
                .orElseThrow(() -> new OlleUException(404, "친구 요청을 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
        Follower follower = followerRepository.findByUserAndFollowerUser(user, friend)
                .orElseThrow(() -> new OlleUException(404, "친구 요청을 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        if (!following.getStatus().equals(OlleUEnum.FriendStatus.INVITE)) {
            throw new OlleUException(400, "이미 친구 요청이 처리되었습니다.", HttpStatus.BAD_REQUEST);
        }

        if (!follower.getStatus().equals(OlleUEnum.FriendStatus.INVITE)) {
            throw new OlleUException(400, "이미 친구 요청이 처리되었습니다.", HttpStatus.BAD_REQUEST);
        }

        following.acceptFriend();
        follower.acceptFriend();
    }

    @Transactional
    public void denyFriend(Long userId, Long friendUserId) {
        User user = this.findById(userId);
        User friend = this.findById(friendUserId);

        Following following = followingRepository.findByUserAndFollowingUser(friend, user)
                .orElseThrow(() -> new OlleUException(404, "친구 요청을 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
        Follower follower = followerRepository.findByUserAndFollowerUser(user, friend)
                .orElseThrow(() -> new OlleUException(404, "친구 요청을 찾을 수 없습니다.", HttpStatus.NOT_FOUND));

        if (!following.getStatus().equals(OlleUEnum.FriendStatus.INVITE)) {
            throw new OlleUException(400, "이미 친구 요청이 처리되었습니다.", HttpStatus.BAD_REQUEST);
        }

        if (!follower.getStatus().equals(OlleUEnum.FriendStatus.INVITE)) {
            throw new OlleUException(400, "이미 친구 요청이 처리되었습니다.", HttpStatus.BAD_REQUEST);
        }

        following.denyFriend();
        follower.denyFriend();
    }

    @Transactional
    public void deleteFriend(Long userId, Long friendUserId) {
        User user = this.findById(userId);
        User friend = this.findById(friendUserId);

        Optional<Following> following = followingRepository.findByUserAndFollowingUser(friend, user);
        Optional<Follower> follower = followerRepository.findByUserAndFollowerUser(user, friend);

        if (!areFriends(following, follower)) {
            following = followingRepository.findByUserAndFollowingUser(user, friend);
            follower = followerRepository.findByUserAndFollowerUser(friend, user);
        }

        if (areFriends(following, follower)) {
            deleteFriendship(following, follower);
        } else {
            throw new OlleUException(400, "친구가 아닙니다.", HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> {
            log.debug("UserService.getUserInfo Error Occur, Input:{}", id);
            return new OlleUException(404, "유저를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
        });
    }

    private boolean areFriends(Optional<Following> following, Optional<Follower> follower) {
        return following.isPresent() &&
                follower.isPresent() &&
                following.get().getStatus().equals(OlleUEnum.FriendStatus.FRIEND) &&
                follower.get().getStatus().equals(OlleUEnum.FriendStatus.FRIEND);
    }

    private void deleteFriendship(Optional<Following> following, Optional<Follower> follower) {
        following.get().deleteFriend();
        follower.get().deleteFriend();
    }
}
