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

    private Long roomId;
    private String userName;
    private String userInfo;
    private String message;
    private String chatTime;

}

