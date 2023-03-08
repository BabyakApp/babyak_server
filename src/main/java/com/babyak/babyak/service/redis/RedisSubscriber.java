package com.babyak.babyak.service.redis;

import com.babyak.babyak.dto.chat.ChatResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber {

    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messagingTemplate;

    /* Redis에서 메세지가 publish 되면 대기하고 있던 RedisSubscriber가 해당 메세지 받아서 처리 */
    public void sendMessage (String publishMessage) {
        try {
            // ChatResponse 객체로 맵핑
            ChatResponse chatResponse = objectMapper.readValue(publishMessage, ChatResponse.class);
            // 채팅방 subscribe한 클라이언트들에게 메세지 발송
            messagingTemplate.convertAndSend("/sub/chat/room/" + chatResponse.getRoomId(), chatResponse);
        } catch (JsonMappingException e) {
            //throw new RuntimeException(e);
            log.error("[Exception] {}", e);
        } catch (JsonProcessingException e) {
            //throw new RuntimeException(e);
            log.error("[Exception] {}", e);
        }
    }
}
