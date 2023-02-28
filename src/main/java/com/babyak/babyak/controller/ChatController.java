package com.babyak.babyak.controller;

import com.babyak.babyak.domain.chat.Chat;
import com.babyak.babyak.domain.chat.Chatroom;
import com.babyak.babyak.domain.user.User;
import com.babyak.babyak.dto.chat.ChatDto;
import com.babyak.babyak.dto.chat.ChatroomRequest;
import com.babyak.babyak.dto.chat.ChatroomResponse;
import com.babyak.babyak.security.oauth2.PrincipalDetails;
import com.babyak.babyak.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatService chatService;

    /* 채팅방 생성 */
    @PostMapping("/room")
    @ResponseBody
    public ResponseEntity<ChatroomResponse> createChatRoom(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody ChatroomRequest request) {
        Integer userId = principalDetails.getUser().getUserId();
        return ResponseEntity.ok(chatService.createChatroom(userId, request));
    }

//    @MessageMapping("/message")
//    public void message (ChatDto chat) {
//        if (ChatDto.MessageType.ENTER.equals(chat.getType())) {
//            chat.setMessage(chat.getUserId() + "님이 들어왔습니다.");
//        }
//        messagingTemplate.convertAndSend("sub/chat/room/" + chat.getRoomId(), chat);
//    }

    /* 채팅방 전체 목록 반환 */
    @GetMapping("/allRoom")
    @ResponseBody
    public ResponseEntity<List<Chatroom>> getAllChatroom() {
        return ResponseEntity.ok(chatService.getAllChatroom());
    }


}
