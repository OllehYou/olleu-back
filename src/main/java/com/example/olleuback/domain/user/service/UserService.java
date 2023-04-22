package com.example.olleuback.domain.user.service;

import com.example.olleuback.common.exception.OlleUException;
import com.example.olleuback.domain.user.dto.CreateUserDto;
import com.example.olleuback.domain.user.dto.LoginUserDto;
import com.example.olleuback.domain.user.entity.AuthCode;
import com.example.olleuback.domain.user.dto.UpdateUserInfoDto;
import com.example.olleuback.domain.user.dto.UserDto;
import com.example.olleuback.domain.user.entity.User;
import com.example.olleuback.domain.user.repository.AuthCodeRepository;
import com.example.olleuback.domain.user.repository.UserRepository;
import com.example.olleuback.utils.service.email.EmailService;
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
    private final AuthCodeRepository authCodeRepository;
    private final EmailService emailService;
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

    @Transactional
    public boolean updateUserInfo(UpdateUserInfoDto updateUserInfoDto) {
        User user = this.findById(updateUserInfoDto.getId());
        String newNickname = this.addRandomNumberToNickname(updateUserInfoDto.getNickname());
        user.updateUserInfo(newNickname);
        return true;
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

    @Transactional
    public void changePassword(Long id, String newPassword) {
        User user = this.findById(id);
        //TODO password 엔코딩 추가
        user.changePassword(newPassword);
    }

    public void requestAuthCode(Long userId) {
        User user = this.findById(userId);
        Random random = new Random();
        int randomNumber = random.nextInt(1000000);
        String authCode = String.format("%06d", randomNumber);
        AuthCode savedAuthCode = authCodeRepository.save(AuthCode.ofCreate(user.getId(), authCode));

        emailService.sendMailAuthCode(user.getEmail(), savedAuthCode.getAuthCode());
    }
    @Transactional
    public void confirmAuthCode(Long userId, String code) {
        AuthCode authCode = authCodeRepository.findByUserId(userId).orElseThrow(() -> {
            log.debug("UserService.confirmAuthCode Error Occur : Entity NotFound, Input:{}",
                      userId);
            return new OlleUException(404, "발급된 인증코드가 없습니다.", HttpStatus.NOT_FOUND);
        });

        if(!authCode.getAuthCode().equals(code)) {
            log.debug("UserService.confirmAuthCode Error Occur : code not equals savedAuthCode,"
                              + " Input userId: {}, code:{}, savedCode:{}",
                      userId, code, authCode.getAuthCode());
            throw new OlleUException(400, "인증코드가 틀립니다.", HttpStatus.BAD_REQUEST);
        }
        authCodeRepository.delete(authCode);
    }

    @Transactional(readOnly = true)
    public UserDto getUserInfo(Long id) {
        User user = this.findById(id);
        return UserDto.ofCreate(user.getId(), user.getEmail(), user.getNickname());
    }
    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> {
            log.debug("UserService.getUserInfo Error Occur, Input:{}", id);
            return new OlleUException(404, "유저를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
        });
    }
}
