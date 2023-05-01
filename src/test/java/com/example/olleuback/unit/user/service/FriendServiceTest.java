package com.example.olleuback.unit.user.service;

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
import com.example.olleuback.domain.user.service.FriendService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class FriendServiceTest {
    @InjectMocks
    FriendService friendService;
    @Mock
    UserRepository userRepository;
    @Mock
    FollowingRepository followingRepository;
    @Mock
    FollowerRepository followerRepository;

    @Test
    @DisplayName("친구 초대 단위 테스트")
    void follow() {
        //given
        User user = User.ofSignup("user1@test.com", "user1", "password");
        User friend = User.ofSignup("user2@test.com", "user2", "password");
        Following following = Following.ofCreate(user, friend);
        Follower follower = Follower.ofCreate(friend, user);

        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(userRepository.findById(2L)).willReturn(Optional.of(friend));

        given(followingRepository.existsByUserIdAndFollowingUserId(1L, 2L)).willReturn(false);
        given(followerRepository.existsByUserIdAndFollowerUserId(2L, 1L)).willReturn(false);

        given(followingRepository.save(any())).willReturn(following);
        given(followerRepository.save(any())).willReturn(follower);

        //when
        boolean result = friendService.makeFriend(1L, 2L);

        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("친구 수락 단위 테스트")
    void acceptFriend(){
        //given
        User user = User.ofSignup("user1@test.com", "user1", "password");
        User friend = User.ofSignup("user2@test.com", "user2", "password");
        Following following = Following.ofCreate(user, friend);
        Follower follower = Follower.ofCreate(friend, user);

        FriendAcceptDto friendAcceptDto = new FriendAcceptDto(1L, 2L);

        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(userRepository.findById(2L)).willReturn(Optional.of(friend));
        given(followingRepository.findByUserAndFollowingUser(friend, user)).willReturn(Optional.of(following));
        given(followerRepository.findByUserAndFollowerUser(user, friend)).willReturn(Optional.of(follower));

        //when
        friendService.acceptFriend(friendAcceptDto);

        //then
        assertThat(following.getStatus()).isEqualTo(OlleUEnum.FriendStatus.FRIEND);
        assertThat(follower.getStatus()).isEqualTo(OlleUEnum.FriendStatus.FRIEND);
    }

    @Test
    @DisplayName("친구 거절 단위 테스트")
    void denyFriend() {
        //given
        User user = User.ofSignup("user1@test.com", "user1", "password");
        User friend = User.ofSignup("user2@test.com", "user2", "password");
        Following following = Following.ofCreate(user, friend);
        Follower follower = Follower.ofCreate(friend, user);

        FriendDenyDto friendDenyDto = new FriendDenyDto(1L, 2L);

        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(userRepository.findById(2L)).willReturn(Optional.of(friend));
        given(followingRepository.findByUserAndFollowingUser(friend, user)).willReturn(Optional.of(following));
        given(followerRepository.findByUserAndFollowerUser(user, friend)).willReturn(Optional.of(follower));

        //when
        friendService.denyFriend(friendDenyDto);

        //then
        assertThat(following.getStatus()).isEqualTo(OlleUEnum.FriendStatus.DELETE);
        assertThat(follower.getStatus()).isEqualTo(OlleUEnum.FriendStatus.DELETE);
    }

    @Test
    @DisplayName("친구요청을 보냈을 때 친구 삭제 단위 테스트")
    void deleteFriendWhenFriendRequestSent() {
        //given
        User user = User.ofSignup("user1@test.com", "user1", "password");
        User friend = User.ofSignup("user2@test.com", "user2", "password");
        Following following = Following.ofCreate(user, friend);
        Follower follower = Follower.ofCreate(friend, user);

        following.acceptFriend();
        follower.acceptFriend();

        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(userRepository.findById(2L)).willReturn(Optional.of(friend));

        given(followingRepository.findByUserAndFollowingUser(friend, user)).willReturn(Optional.empty());
        given(followerRepository.findByUserAndFollowerUser(user, friend)).willReturn(Optional.empty());

        given(followingRepository.findByUserAndFollowingUser(user, friend)).willReturn(Optional.of(following));
        given(followerRepository.findByUserAndFollowerUser(friend, user)).willReturn(Optional.of(follower));

        FriendDeleteDto friendDeleteDto = new FriendDeleteDto(1L, 2L);

        //when
        friendService.deleteFriend(friendDeleteDto);

        //then
        assertThat(following.getStatus()).isEqualTo(OlleUEnum.FriendStatus.DELETE);
        assertThat(follower.getStatus()).isEqualTo(OlleUEnum.FriendStatus.DELETE);
    }

    @Test
    @DisplayName("친구요청을 받았을 때 친구 삭제 단위 테스트")
    void deleteFriendWhenFriendRequestRecieved() {
        //given
        User user = User.ofSignup("user1@test.com", "user1", "password");
        User friend = User.ofSignup("user2@test.com", "user2", "password");
        Following following = Following.ofCreate(user, friend);
        Follower follower = Follower.ofCreate(friend, user);

        following.acceptFriend();
        follower.acceptFriend();

        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(userRepository.findById(2L)).willReturn(Optional.of(friend));

        given(followingRepository.findByUserAndFollowingUser(friend, user)).willReturn(Optional.of(following));
        given(followerRepository.findByUserAndFollowerUser(user, friend)).willReturn(Optional.of(follower));

        FriendDeleteDto friendDeleteDto = new FriendDeleteDto(1L, 2L);

        //when
        friendService.deleteFriend(friendDeleteDto);

        //then
        assertThat(following.getStatus()).isEqualTo(OlleUEnum.FriendStatus.DELETE);
        assertThat(follower.getStatus()).isEqualTo(OlleUEnum.FriendStatus.DELETE);
    }
}
