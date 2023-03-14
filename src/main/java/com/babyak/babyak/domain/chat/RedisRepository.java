package com.babyak.babyak.domain.chat;

import com.babyak.babyak.DTO.chat.RedisChatroom;
import com.babyak.babyak.service.redis.RedisSubscriber;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Repository
public class RedisRepository {

    // Redis
    private static final String CHAT_ROOMS = "CHAT_ROOM";
    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, Long, RedisChatroom> opsHashChatRoom;


    @PostConstruct
    private void init() {
        opsHashChatRoom = redisTemplate.opsForHash();
    }

    /* 채팅방 생성: 서버 간 채팅방 공유를 위해 redis hash에 저장 */
    public void createChatroom(Long id, String name) {
        RedisChatroom chatroom = RedisChatroom.create(id, name);
        opsHashChatRoom.put(CHAT_ROOMS, chatroom.getId(), chatroom);
    }

}
