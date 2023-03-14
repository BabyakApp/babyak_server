package com.babyak.babyak.DTO.chat;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatroomRequest {

    private String roomName;
    private String description;
    private int maxPeople;

}
