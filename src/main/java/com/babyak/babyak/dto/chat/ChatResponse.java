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

    private String userName;
    private String userMajor;
    private String userYear;
    private String message;
    private String chatTime;

}

