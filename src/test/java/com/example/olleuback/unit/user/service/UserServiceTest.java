package com.example.olleuback.unit.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.example.olleuback.domain.user.dto.CreateUserDto;
import com.example.olleuback.domain.user.dto.LoginUserDto;
import com.example.olleuback.domain.user.dto.LoginUserDto.Response;
import com.example.olleuback.domain.user.entity.User;
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
}
