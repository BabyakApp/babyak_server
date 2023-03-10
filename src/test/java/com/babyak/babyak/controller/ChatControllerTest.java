package com.babyak.babyak.controller;

import com.babyak.babyak.MessageFrameHandler;
import com.babyak.babyak.StompSupport;
import com.babyak.babyak.domain.chat.Chatroom;
import com.babyak.babyak.domain.chat.ChatroomRepository;
import com.babyak.babyak.domain.user.User;
import com.babyak.babyak.dto.chat.ChatRequest;
import com.babyak.babyak.dto.chat.ChatResponse;
import com.babyak.babyak.security.jwt.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.*;

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
