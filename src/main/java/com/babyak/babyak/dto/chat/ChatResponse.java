package com.babyak.babyak.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {

    public enum MessageType {
        ENTER, EXIT, TALK
    }

    private Long roomId;
    private MessageType messageType;
    private String userName;
    private String userInfo;
    private String message;
    private String chatTime;

}

