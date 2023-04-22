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
public class Following {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "following_user_id", referencedColumnName = "id")
    private User followingUser;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private FriendStatus status;

    public static Following ofCreate(User user, User followingUser) {
        Following following = new Following();
        following.user = user;
        following.followingUser = followingUser;
        following.status = FriendStatus.INVITE;
        return following;
    }
}
