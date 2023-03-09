package com.babyak.babyak.dto.chat;

import com.babyak.babyak.domain.chat.Chatroom;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class RedisChatroom implements Serializable {

    private static final long serialVersionUID = 1886723345619283745L;

    private Long id;
    private String roomName;

    public static RedisChatroom create(Long id, String roomName) {
        RedisChatroom redisChatroom = new RedisChatroom();
        redisChatroom.id = id;
        redisChatroom.roomName = roomName;
        return redisChatroom;
    }
}
