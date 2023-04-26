package com.example.olleuback.unit.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.example.olleuback.domain.user.dto.CreateUserDto;
import com.example.olleuback.domain.user.dto.FriendAcceptDto;
import com.example.olleuback.domain.user.dto.LoginUserDto;
import com.example.olleuback.domain.user.dto.UpdateUserInfoDto;
import com.example.olleuback.domain.user.dto.UserDto;
import com.example.olleuback.domain.user.entity.Follower;
import com.example.olleuback.domain.user.entity.Following;
import com.example.olleuback.domain.user.entity.User;
import com.example.olleuback.domain.user.repository.FollowerRepository;
import com.example.olleuback.domain.user.repository.FollowingRepository;
import com.example.olleuback.domain.user.repository.UserRepository;
import com.example.olleuback.domain.user.service.UserService;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    UserService userService;
    @Mock
    UserRepository userRepository;
    @Mock
    FollowingRepository followingRepository;
    @Mock
    FollowerRepository followerRepository;

    @Test
    @DisplayName("회원가입 서비스 단위 테스트")
    void signup() {
        //given
        CreateUserDto createUserDto = new CreateUserDto();
        createUserDto.setEmail("email@gmail.com");
        createUserDto.setNickname("nickname");
        createUserDto.setPassword("password");

        given(userRepository.existsByEmail(createUserDto.getEmail())).willReturn(false);
        given(userRepository.existsByNickname(any())).willReturn(false);
        //when
        userService.signup(createUserDto);

        //then
    }

    @Test
    @DisplayName("로그인 서비스 단위 테스트")
    void login() {
        //given
        LoginUserDto.Request request = new LoginUserDto.Request();
        request.setEmail("email@naver.com");
        request.setPassword("password");

        User user = User.ofSignup(request.getEmail(), "nickname", request.getPassword());

        given(userRepository.findByEmail(request.getEmail())).willReturn(Optional.of(user));
        //when
        LoginUserDto.Response response = userService.login(request);

        //then
        assertThat(response).isNotNull();
    }

    @Test
    @DisplayName("유저 정보 업데이트 서비스 단위 테스트")
    void updateUserInfo() {
        //given
        UpdateUserInfoDto userInfoDto = new UpdateUserInfoDto();
        userInfoDto.setId(1L);
        userInfoDto.setNickname("updateNickname");

        User user = User.ofSignup("email@naver.com", "nickname", "password");
        given(userRepository.findById(any())).willReturn(Optional.of(user));

        //when
        boolean result = userService.updateUserInfo(userInfoDto);

        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("유저 조회 단위 테스트")
    void getUserInfo() {
        //given

        User user = User.ofSignup("email@naver.com", "nickname", "password");

        given(userRepository.findById(any())).willReturn(Optional.of(user));
        //when
        UserDto response = userService.getUserInfo(1L);

        //then
        assertThat(response).isNotNull();
        assertThat(response.getNickname()).isEqualTo("nickname");
        assertThat(response.getEmail()).isEqualTo("email@naver.com");
    }

    @Test
    @DisplayName("친구 초대 단위 테스트")
    void follow(){
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
        boolean result = userService.follow(1L, 2L);

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
        userService.acceptFriend(friendAcceptDto);

    }
}
