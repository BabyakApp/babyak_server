package com.babyak.babyak.controller;

import com.babyak.babyak.domain.chat.Chatroom;
import com.babyak.babyak.domain.user.User;
import com.babyak.babyak.dto.chat.*;
import com.babyak.babyak.security.oauth2.PrincipalDetails;
import com.babyak.babyak.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    /* 채팅방 생성 */
    @PostMapping("/room")
    @ResponseBody
    public ResponseEntity<ChatroomResponse> createChatRoom(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody ChatroomRequest request) {
        User user = principalDetails.getUser();
        return ResponseEntity.ok(chatService.createChatroom(user, request));
    }


    /* 채팅방 입장 */
    @GetMapping("/check/{roomId}")
    @ResponseBody
    public ResponseEntity<CheckResponse> checkEnterStatus(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable Long roomId
    ) {
        User user = principalDetails.getUser();
        return ResponseEntity.ok(
                chatService.checkEnterStatus(user, roomId)
        );
    }


    /* 채팅 전송 */
    @MessageMapping("/message")
    public void message (@AuthenticationPrincipal PrincipalDetails principalDetails, ChatRequest chat) {
        User user = principalDetails.getUser();
        chatService.sendMessage(user, chat);
    }


    /* 채팅방 전체 목록 반환 */
    @GetMapping("/allRoom")
    @ResponseBody
    public ResponseEntity<List<Chatroom>> getAllChatroom() {
        return ResponseEntity.ok(chatService.getAllChatroom());
    }


    /* 참여한 채팅방 목록 */
    @GetMapping("/user/list")
    @ResponseBody
    public ResponseEntity<List<ChatroomListResponse>> getUserChatroomList(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        Integer userId = principalDetails.getUser().getUserId();
        return ResponseEntity.ok(chatService.getUserChatroomList(userId));
    }
}
