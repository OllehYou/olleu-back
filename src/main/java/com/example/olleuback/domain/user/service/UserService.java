package com.example.olleuback.domain.user.service;

import com.example.olleuback.common.exception.OlleUException;
import com.example.olleuback.domain.user.dto.CreateUserDto;
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

    @Transactional(rollbackFor = Exception.class)
    public void signup(CreateUserDto createUserDto) {
        if(userRepository.existsByEmail(createUserDto.getEmail())) {
            throw new OlleUException(404, "이미 존재하는 이메일입니다.", HttpStatus.BAD_REQUEST);
        }

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
}
