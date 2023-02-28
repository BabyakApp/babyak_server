package com.babyak.babyak.domain.chat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class Chat {

    private Long userId; // 채팅 보낸 userId
    private LocalDateTime chatTime; // 채팅 보낸 시간
    private String message; // 채팅 내용

}
