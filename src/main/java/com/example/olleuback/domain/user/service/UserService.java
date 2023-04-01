package com.example.olleuback.domain.user.service;

import com.example.olleuback.common.exception.OlleUException;
import com.example.olleuback.domain.user.dto.CreateUserDto;
import com.example.olleuback.domain.user.dto.LoginUserDto;
import com.example.olleuback.domain.user.entity.User;
import com.example.olleuback.domain.user.repository.UserRepository;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    //TODO BCryptPasswordEncoder 추가


    @Transactional(rollbackFor = Exception.class)
    public void signup(CreateUserDto createUserDto) {
        if(userRepository.existsByEmail(createUserDto.getEmail())) {
            throw new OlleUException(404, "이미 존재하는 이메일입니다.", HttpStatus.BAD_REQUEST);
        }
        //TODO 비밀번호 인코딩

        String nickname = addRandomNumberToNickname(createUserDto.getNickname());
        User user = User.ofSignup(createUserDto.getEmail(), nickname, createUserDto.getPassword());

        userRepository.save(user);
    }

    private String addRandomNumberToNickname(String originNickname) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(originNickname);
        sb.append("#").append(String.format("%04d", random.nextInt(0, 10000)));
        while(userRepository.existsByNickname(sb.toString())){
            sb.replace(sb.indexOf("#") + 1, sb.length(), String.format("%04d", random.nextInt(0, 10000)));
        }
        return sb.toString();
    }

    @Transactional
    public LoginUserDto.Response login(LoginUserDto.Request loginUserRequestDto) {
        User user = userRepository.findByEmail(loginUserRequestDto.getEmail()).orElseThrow(
                () -> new OlleUException(404, "이메일이나 비밀번호가 일치하지 않습니다.", HttpStatus.NOT_FOUND));
        //TODO 엔코딩 추가

        //TODO 토큰 생성

        return LoginUserDto.Response.ofCreate(user.getId());
    }
}
