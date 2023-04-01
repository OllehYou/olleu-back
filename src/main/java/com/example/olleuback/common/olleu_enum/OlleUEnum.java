package com.example.olleuback.common.olleu_enum;

import lombok.Getter;

public class OlleUEnum {
    @Getter
    public enum FriendStatus {
        INVITE,
        FRIEND,
        DELETE
    }
    @Getter
    public enum ParticipateStatus {
        INVITE,
        ACCEPT
    }
}
