package com.example.olleuback.unit.user.service;

import com.example.olleuback.common.olleu_enum.OlleUEnum;
import com.example.olleuback.domain.user.dto.UserDto;
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

import java.util.List;
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
    @DisplayName("친구 목록 조회 단위 테스트")
    void getFriends() {
        //given
        User user = User.ofSignup("user1@test.com", "user1", "password");

        User friend1 = User.ofSignup("user2@test.com", "user2", "password");
        User friend2 = User.ofSignup("user3@test.com", "user3", "password");

        Following following1 = Following.ofCreate(user, friend1);
        following1.acceptFriend();

        Following following2 = Following.ofCreate(user, friend2);
        following2.acceptFriend();

        Follower follower1 = Follower.ofCreate(friend1, user);
        follower1.acceptFriend();

        Follower follower2 = Follower.ofCreate(friend2, user);
        follower2.acceptFriend();

        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(followingRepository.findByUser(user)).willReturn(List.of(following1, following2));
        given(followerRepository.findByUser(user)).willReturn(List.of());

        //when
        List<UserDto> result = friendService.getFriends(1L);

        //then
        assertThat(result.size()).isEqualTo(2);

        assertThat(result.get(0).getId()).isEqualTo(friend1.getId());
        assertThat(result.get(0).getEmail()).isEqualTo(friend1.getEmail());
        assertThat(result.get(0).getNickname()).isEqualTo(friend1.getNickname());

        assertThat(result.get(1).getId()).isEqualTo(friend2.getId());
        assertThat(result.get(1).getEmail()).isEqualTo(friend2.getEmail());
        assertThat(result.get(1).getNickname()).isEqualTo(friend2.getNickname());
    }

    @Test
    @DisplayName("친구 정보 조회 단위 테스트")
    void getFriend() {
        //given
        User user = User.ofSignup("user1@test.com", "user1", "password");
        User friend = User.ofSignup("user2@test.com", "user2", "password");
        Following following = Following.ofCreate(user, friend);
        Follower follower = Follower.ofCreate(friend, user);

        following.acceptFriend();
        follower.acceptFriend();

        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(userRepository.findById(2L)).willReturn(Optional.of(friend));

        given(followingRepository.findByUserAndFollowingUser(user, friend)).willReturn(Optional.empty());
        given(followerRepository.findByUserAndFollowerUser(friend, user)).willReturn(Optional.empty());

        given(followingRepository.findByUserAndFollowingUser(friend, user)).willReturn(Optional.of(following));
        given(followerRepository.findByUserAndFollowerUser(user, friend)).willReturn(Optional.of(follower));

        //when
        UserDto result = friendService.getFriend(1L, 2L);

        //then
        assertThat(result.getId()).isEqualTo(friend.getId());
        assertThat(result.getEmail()).isEqualTo(friend.getEmail());
        assertThat(result.getNickname()).isEqualTo(friend.getNickname());
    }

    @Test
    @DisplayName("친구 초대 단위 테스트")
    void makeFriend() {
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
    void acceptFriend() {
        //given
        User user = User.ofSignup("user1@test.com", "user1", "password");
        User friend = User.ofSignup("user2@test.com", "user2", "password");
        Following following = Following.ofCreate(user, friend);
        Follower follower = Follower.ofCreate(friend, user);

        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(userRepository.findById(2L)).willReturn(Optional.of(friend));
        given(followingRepository.findByUserAndFollowingUser(friend, user)).willReturn(Optional.of(following));
        given(followerRepository.findByUserAndFollowerUser(user, friend)).willReturn(Optional.of(follower));

        //when
        friendService.acceptFriend(1L, 2L);

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

        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(userRepository.findById(2L)).willReturn(Optional.of(friend));
        given(followingRepository.findByUserAndFollowingUser(friend, user)).willReturn(Optional.of(following));
        given(followerRepository.findByUserAndFollowerUser(user, friend)).willReturn(Optional.of(follower));

        //when
        friendService.denyFriend(1L, 2L);

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

        //when
        friendService.deleteFriend(1L, 2L);

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

        //when
        friendService.deleteFriend(1L, 2L);

        //then
        assertThat(following.getStatus()).isEqualTo(OlleUEnum.FriendStatus.DELETE);
        assertThat(follower.getStatus()).isEqualTo(OlleUEnum.FriendStatus.DELETE);
    }
}
