package com.babyak.babyak.dto.chat;

import com.babyak.babyak.domain.chat.Chat;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatDto {

    public enum MessageType {
        ENTER, CHAT, LEAVE
    }

    private MessageType type; // 메세지 타입
    private Long roomId;
    private Long userId; // 채팅 보낸 userId
    private LocalDateTime chatTime; // 채팅 보낸 시간
    private String message; // 채팅 내용

}
