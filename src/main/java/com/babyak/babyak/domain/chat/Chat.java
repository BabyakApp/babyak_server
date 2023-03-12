package com.babyak.babyak.domain.chat;


import lombok.*;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class Chat {

    public enum MessageType {
        ENTER, EXIT, TALK
    }
    private MessageType messageType;
    private Integer userId; // 채팅 보낸 userId
    private String nickname;
    private String userInfo;
    private String message; // 채팅 내용
    private String chatTime; // 채팅 보낸 시간

}
