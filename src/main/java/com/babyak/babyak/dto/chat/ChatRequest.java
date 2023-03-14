package com.babyak.babyak.DTO.chat;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequest {

//    public enum MessageType {
//        ENTER, CHAT, LEAVE
//    }

    private Long roomId;
    private String message; // 채팅 내용
//    private MessageType type; // 메세지 타입

}
