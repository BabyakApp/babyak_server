package com.babyak.babyak.controller;

import com.babyak.babyak.StompSupport;
import com.babyak.babyak.domain.chat.Chatroom;
import com.babyak.babyak.mongo.ChatroomRepository;
import com.babyak.babyak.dto.chat.ChatRequest;
import com.babyak.babyak.dto.chat.ChatResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.stomp.StompFrameHandler;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Slf4j
public class ChatControllerTest extends StompSupport {

    @Autowired
    private ChatroomRepository chatroomRepository;

    @Test
    public void message() throws ExecutionException, InterruptedException, TimeoutException {
//        /* GIVEN */
//        MessageFrameHandler<ChatResponse[]> handler = new MessageFrameHandler<>(ChatResponse[].class);
//        this.stompSession.subscribe("/sub/chat/message/1", handler);
//
//        /* WHEN */
//        ChatRequest request = new ChatRequest(1L, "TEST MESSAGE");
//        this.stompSession.send("/pub/chat/message", request);
//
//        /* THEN */
//        List<ChatResponse> chatResponseList
//                = List.of(handler.getCompletableFuture().get(3, TimeUnit.SECONDS));
//
//        assertThat(chatResponseList).isNotEmpty();
//        assertThat(chatResponseList.size()).isGreaterThan(0);

//        User user = new User(3, 1976415, "s2sooas2@ewhain.net", "컴댕",
//                "소프트웨어학부", "컴퓨터공학전공", 0, "USER");
        ChatRequest request = new ChatRequest(5L, "TEST MESSAGE");
        this.stompSession.send("pub/chat/message", request);

        ChatResponse response = new ChatResponse();
        this.stompSession.subscribe("/sub/chat/message/1", (StompFrameHandler) response);

        Chatroom room = chatroomRepository.findByIdx(1L);
        System.out.println("id: " + room.getIdx());
        System.out.println("chat: " + room.getChats());
    }

}
