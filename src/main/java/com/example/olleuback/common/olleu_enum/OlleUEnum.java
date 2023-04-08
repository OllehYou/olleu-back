package com.example.olleuback.common.olleu_enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class OlleUEnum {
    @Getter
    @AllArgsConstructor
    public enum FriendStatus implements EnumType{
        INVITE("초대"),
        FRIEND("친구"),
        DELETE("삭제");
        private final String description;
        @Override
        public String getName() {
            return this.name();
        }
        @Override
        public String getDescription() {
            return this.description;
        }
    }
    @Getter
    @AllArgsConstructor
    public enum ParticipateStatus implements EnumType{
        INVITE("초대"),
        ACCEPT("수락");
        private final String description;
        @Override
        public String getName() {
            return this.name();
        }
        @Override
        public String getDescription() {
            return this.description;
        }
    }
}
