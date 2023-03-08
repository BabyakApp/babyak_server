package com.babyak.babyak.domain.chat;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class Chat {

    private Integer userId; // 채팅 보낸 userId
    private String nickname;
    private String message; // 채팅 내용
    private String chatTime; // 채팅 보낸 시간

}
