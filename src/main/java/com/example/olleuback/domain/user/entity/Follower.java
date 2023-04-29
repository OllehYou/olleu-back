package com.example.olleuback.domain.user.entity;

import com.example.olleuback.common.olleu_enum.OlleUEnum.FriendStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Follower {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "follower_user_id", referencedColumnName = "id")
    private User followerUser;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private FriendStatus status;

    public static Follower ofCreate(User user, User followerUser) {
        Follower follower = new Follower();
        follower.user = user;
        follower.followerUser = followerUser;
        follower.status = FriendStatus.INVITE;
        return follower;
    }

    public void acceptFriend() {
        this.status = FriendStatus.FRIEND;
    }

    public void denyFriend() {
        this.status = FriendStatus.DELETE;
    }

    public void deleteFriend() {
        this.status = FriendStatus.DELETE;
    }
}
