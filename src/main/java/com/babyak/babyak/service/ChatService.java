package com.babyak.babyak.service;

import com.babyak.babyak.domain.chat.Chatroom;
import com.babyak.babyak.domain.chat.ChatroomRepository;
import com.babyak.babyak.dto.chat.ChatroomRequest;
import com.babyak.babyak.dto.chat.ChatroomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final SequenceGeneratorService sequenceGeneratorService;
    private final ChatroomRepository chatroomRepository;

    /* 채팅방 생성 */
    public ChatroomResponse createChatroom (Integer userId, ChatroomRequest request) {
        Chatroom chatroom = new Chatroom();

        // DB 저장
        chatroom.setIdx(sequenceGeneratorService.generateSequence(Chatroom.SEQUENCE_NAME));
        chatroom.setRoomName(request.getRoomName());
        chatroom.setDescription(request.getDescription());
        chatroom.setMaxPeople(request.getMaxPeople());
        chatroom.setCurrentNumber(0);
        List<Integer> userList = new ArrayList<>();
        userList.add(userId);
        chatroom.setListOfUsers(userList);
        Chatroom room = chatroomRepository.save(chatroom);

        ChatroomResponse response = new ChatroomResponse(room.getIdx());
        return response;
    }

    /* 채팅방 전체 목록 반환 */
    public List<Chatroom> getAllChatroom() {
        List<Chatroom> chatroomList = chatroomRepository.findAll();
        return chatroomList;
    }

}
