package com.example.olleuback.domain.user.service;

import com.example.olleuback.common.exception.OlleUException;
import com.example.olleuback.common.olleu_enum.OlleUEnum;
import com.example.olleuback.domain.user.dto.FriendAcceptDto;
import com.example.olleuback.domain.user.dto.FriendDeleteDto;
import com.example.olleuback.domain.user.dto.FriendDenyDto;
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

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FriendService {
    private final UserRepository userRepository;
    private final FollowingRepository followingRepository;
    private final FollowerRepository followerRepository;

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> {
            log.debug("UserService.getUserInfo Error Occur, Input:{}", id);
            return new OlleUException(404, "유저를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
        });
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
    public void acceptFriend(FriendAcceptDto friendAcceptDto) {
        User user = this.findById(friendAcceptDto.getMyId());
        User friend = this.findById(friendAcceptDto.getFriendId());

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
    public void denyFriend(FriendDenyDto friendDenyDto) {
        User user = this.findById(friendDenyDto.getMyId());
        User friend = this.findById(friendDenyDto.getFriendId());

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
    public void deleteFriend(FriendDeleteDto friendDeleteDto) {
        User user = this.findById(friendDeleteDto.getUserId());
        User friend = this.findById(friendDeleteDto.getFriendId());

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
