package com.example.olleuback.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    @Column(name = "nickname", unique = true, nullable = false)
    private String nickname;
    @Column(name = "password", nullable = false)
    private String password;
    @OneToMany(mappedBy = "user")
    private List<Follower> followers = new ArrayList<>();
    @OneToMany(mappedBy = "user")
    private List<Following> followings = new ArrayList<>();

    public static User ofSignup(String email, String nickname, String password) {
        User user = new User();
        user.email = email;
        user.nickname = nickname;
        user.password = password;
        return user;
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;

    public void updateUserInfo(String nickname) {
        this.nickname = nickname;
    }
}
