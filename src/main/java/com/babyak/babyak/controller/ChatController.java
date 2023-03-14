package com.babyak.babyak.controller;

import com.babyak.babyak.domain.chat.Chat;
import com.babyak.babyak.domain.chat.ChatInfoMapping;
import com.babyak.babyak.domain.chat.Chatroom;
import com.babyak.babyak.domain.user.User;
import com.babyak.babyak.DTO.ResponseDTO;
import com.babyak.babyak.DTO.chat.*;
import com.babyak.babyak.security.oauth2.PrincipalDetails;
import com.babyak.babyak.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    @PostMapping("/room/{roomId}")
    @ResponseBody
    public ResponseEntity createChatRoom(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody ChatroomRequest request,
            @PathVariable Long roomId) {
        User user = principalDetails.getUser();
        return new ResponseEntity(ResponseDTO.response(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                chatService.createChatroom(user, request, roomId)
        ), HttpStatus.OK);
    }


    /* 채팅방 입장 */
    @GetMapping("/enter/{roomId}")
    @ResponseBody
    public ResponseEntity enterChatroom (
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable Long roomId
    ) {
        User user = principalDetails.getUser();
        return new ResponseEntity(ResponseDTO.response(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                chatService.enterChatroom(user, roomId)
        ), HttpStatus.OK);
    }


    /* 채팅 전송 */
    @MessageMapping("/message")
    public void message(@AuthenticationPrincipal PrincipalDetails principalDetails, ChatRequest chat) {
        User user = principalDetails.getUser();
        chatService.sendMessage(user, chat);
    }


    /* 채팅방 전체 목록 반환 */
    @GetMapping("/allRoom")
    @ResponseBody
    public ResponseEntity<List<Chatroom>> getAllChatroom() {

        return new ResponseEntity(ResponseDTO.response(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                chatService.getAllChatroom()
        ), HttpStatus.OK);

    }


    /* 참여한 채팅방 목록 */
    @GetMapping("/user/list")
    @ResponseBody
    public ResponseEntity<List<ChatroomListResponse>> getUserChatroomList(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        Integer userId = principalDetails.getUser().getUserId();
        return new ResponseEntity(ResponseDTO.response(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                chatService.getUserChatroomList(userId)
        ), HttpStatus.OK);
    }

    /* 채팅방 나가기 */
    @GetMapping("/leave/{roomId}")
    @ResponseBody
    public ResponseEntity<CheckResponse> leaveChatroom(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable Long roomId
    ) {
        User user = principalDetails.getUser();
        return new ResponseEntity(ResponseDTO.response(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                chatService.leaveChatroom(user, roomId)
        ), HttpStatus.OK);
    }

    /* 채팅방 삭제 */
    @DeleteMapping("/room/{roomId}")
    @ResponseBody
    public ResponseEntity<CheckResponse> deleteChatroom(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable Long roomId
    ) {
        Integer userId = principalDetails.getUser().getUserId();
        return new ResponseEntity(ResponseDTO.response(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                chatService.deleteChatroom(userId, roomId)
        ), HttpStatus.OK);
    }

    /* 채팅방의 채팅 목록 반환 */
    @GetMapping("room/{roomId}")
    @ResponseBody
    public ResponseEntity<List<ChatInfoMapping>> getChatList(
            @PathVariable Long roomId
    ) {
        return new ResponseEntity(ResponseDTO.response(
                HttpStatus.OK.value(),
                HttpStatus.OK.getReasonPhrase(),
                chatService.getChatList(roomId)
        ), HttpStatus.OK);
    }

}