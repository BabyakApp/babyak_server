package com.babyak.babyak.service;

import com.babyak.babyak.domain.chat.Chat;
import com.babyak.babyak.domain.chat.Chatroom;
import com.babyak.babyak.domain.chat.ChatroomRepository;
import com.babyak.babyak.domain.user.User;
import com.babyak.babyak.domain.user.UserRepository;
import com.babyak.babyak.dto.chat.*;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final SequenceGeneratorService sequenceGeneratorService;
    private final ChatroomRepository chatroomRepository;
    private final SimpMessageSendingOperations messagingTemplate;
    private final UserRepository userRepository;

    /* 채팅방 생성 */
    public ChatroomResponse createChatroom (User user, ChatroomRequest request) {
        Chatroom chatroom = new Chatroom();

        // DB 저장
        chatroom.setIdx(sequenceGeneratorService.generateSequence(Chatroom.SEQUENCE_NAME));
        chatroom.setRoomName(request.getRoomName());
        chatroom.setDescription(request.getDescription());
        chatroom.setHostUserId(user.getUserId());
        chatroom.setHostUserName(user.getNickname());
        chatroom.setHostUserInfo(user.getMajor() + " " + user.getStudentId().toString().substring(0, 2));
        chatroom.setMaxPeople(request.getMaxPeople());
        chatroom.setCurrentNumber(1);
        List<Integer> userList = new ArrayList<>();
        userList.add(user.getUserId());
        chatroom.setUserList(userList);
        Chatroom room = chatroomRepository.save(chatroom);

        ChatroomResponse response = new ChatroomResponse(room.getIdx());
        return response;
    }


    /* 입장 조건 확인 */
    public CheckResponse checkEnterStatus(User user, Long roomId) {
        CheckResponse response = new CheckResponse();
        Chatroom room = chatroomRepository.findByIdx(roomId);

        Integer userId = user.getUserId();
        // 이미 참여한 유저인지
        if (room.getUserList().contains(userId)) {
            response.setStatus(false);
            response.setMessage("이미 참여하고 있는 채팅방입니다.");
            return response;
        }

        // 채팅방 정원 최대
        if (room.getCurrentNumber() == room.getMaxPeople()) {
            response.setStatus(false);
            response.setMessage("채팅방 정원이 최대 인원에 도달하였습니다.");
            return response;
        }

        // DB
        room.setCurrentNumber(room.getCurrentNumber() + 1);
        List<Integer> newUserList = room.getUserList();
        newUserList.add(userId);
        room.setUserList(newUserList);
        chatroomRepository.save(room); // db update

        response.setStatus(true);
        response.setMessage(user.getNickname() + "님이 들어오셨습니다.");

        return response;
    }


    /* 채팅 전송 */
    public void sendMessage (User user, ChatRequest request) {

        ChatResponse chatResponse = new ChatResponse(); // response용
        Chat chat = new Chat(); // db용

        String nickname = user.getNickname();
        String userYear = user.getStudentId().toString().substring(0, 2);
        String time = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        );

        Long roomId = request.getRoomId();
        Chatroom room = chatroomRepository.findByIdx(roomId);

        // DB
        chat.setUserId(user.getUserId());
        chat.setNickname(nickname);
        chat.setMessage(request.getMessage());
        chat.setChatTime(time);
        room.setLastChatTime(time); // 마지막 채팅 시간 업데이트
        List<Chat> chatList = room.getChats();
        chatList.add(chat);
        room.setChats(chatList); // 채팅 내역 업데이트
        chatroomRepository.save(room);

        // Response
        chatResponse.setUserName(nickname);
        chatResponse.setUserInfo(nickname + " " + userYear);
        chatResponse.setMessage(request.getMessage());
        chatResponse.setChatTime(time);

        messagingTemplate.convertAndSend("sub/chat/room/" + request.getRoomId(), chatResponse);
    }


    /* 채팅방 전체 목록 반환 */
    public List<Chatroom> getAllChatroom() {
        List<Chatroom> chatroomList = chatroomRepository.findAll();
        return chatroomList;
    }

    /* 참여한 채팅방 목록 반환 */
    public List<ChatroomListResponse> getUserChatroomList(Integer userId) {
        List<Chatroom> chatroomList = chatroomRepository.findChatroomsByUserListContaining(userId);
        List<ChatroomListResponse> responses = chatroomList.stream()
                .map(p -> new ChatroomListResponse(
                        p.getIdx(), p.getRoomName(), p.getDescription(),
                        p.getHostUserName(), p.getHostUserInfo(), p.getLastChatTime()
                ))
                .collect(Collectors.toList());

        return responses;
    }

}
