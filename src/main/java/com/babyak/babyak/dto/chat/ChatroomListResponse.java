package com.babyak.babyak.DTO.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatroomListResponse {
    private Long roomId;
    private String roomName;
    private String description;
    private String hostUserName;
    private String hostUserInfo;
    private String lastChatTime;
}
