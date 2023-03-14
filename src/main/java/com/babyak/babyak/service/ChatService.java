package com.babyak.babyak.service;

import com.babyak.babyak.domain.chat.*;
import com.babyak.babyak.domain.user.User;
import com.babyak.babyak.DTO.chat.*;
import com.babyak.babyak.mongo.ChatroomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    //private final SequenceGeneratorService sequenceGeneratorService;
    private final ChatroomRepository chatroomRepository;
    //private final SimpMessageSendingOperations messagingTemplate;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic channelTopic;
    private final RedisRepository redisRepository;

    /* 채팅방 생성 */
    public ChatroomResponse createChatroom (User user, ChatroomRequest request, Long roomId) {
        Chatroom chatroom = new Chatroom();

        // DB 저장
        //chatroom.setIdx(sequenceGeneratorService.generateSequence(Chatroom.SEQUENCE_NAME));
        chatroom.setIdx(roomId);
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
        List<Chat> chats = new ArrayList<>();
        chatroom.setChats(chats);
        Chatroom room = chatroomRepository.save(chatroom);

        ChatroomResponse response = new ChatroomResponse(room.getIdx());
        redisRepository.createChatroom(room.getIdx(), room.getRoomName());
        return response;
    }


    /* 입장 조건 확인 */
    public CheckResponse enterChatroom (User user, Long roomId) {
        CheckResponse response = new CheckResponse();
        Chatroom room = chatroomRepository.findByIdx(roomId);

        Integer userId = user.getUserId();
        // 이미 참여한 유저인지
        if (room.getUserList().contains(userId)) {
            response.setStatus(true);
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
        String nickname = user.getNickname();
        String userYear = user.getStudentId().toString().substring(0, 2);
        String time = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        );
        String userInfo = user.getMajor() + " " + userYear;

        Chat chat = new Chat();
        chat.setMessageType(Chat.MessageType.ENTER);
        chat.setUserId(user.getUserId());
        chat.setNickname(nickname);
        chat.setUserInfo(userInfo);
        chat.setMessage(nickname + " 님이 들어오셨습니다.");
        chat.setChatTime(time);

        room.setCurrentNumber(room.getCurrentNumber() + 1);
        List<Integer> newUserList = room.getUserList();
        newUserList.add(userId);
        room.setUserList(newUserList);
        room.setLastChatTime(time); // 마지막 채팅 시간 업데이트
        List<Chat> chatList = room.getChats();
        if (chatList == null) {
            chatList = new ArrayList<>();
        }
        chatList.add(chat);
        room.setChats(chatList); // 채팅 내역 업데이트
        chatroomRepository.save(room); // db update


        // STOMP
        ChatResponse chatResponse = new ChatResponse();
        chatResponse.setRoomId(roomId);
        chatResponse.setMessageType(ChatResponse.MessageType.ENTER);
        chatResponse.setUserName(nickname);
        chatResponse.setUserInfo(userInfo);
        chatResponse.setMessage(nickname + " 님이 들어오셨습니다.");
        chatResponse.setChatTime(time);
        redisTemplate.convertAndSend(channelTopic.getTopic(), chatResponse);

        response.setStatus(true);
        response.setMessage(user.getNickname() + " 님이 들어오셨습니다.");

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
        String userInfo = user.getMajor() + " " + userYear;

        Long roomId = request.getRoomId();
        Chatroom room = chatroomRepository.findByIdx(roomId);

        // DB
        chat.setMessageType(Chat.MessageType.TALK);
        chat.setUserId(user.getUserId());
        chat.setNickname(nickname);
        chat.setUserInfo(userInfo);
        chat.setMessage(request.getMessage());
        chat.setChatTime(time);
        room.setLastChatTime(time); // 마지막 채팅 시간 업데이트
        List<Chat> chatList = room.getChats();
        if (chatList == null) {
            chatList = new ArrayList<>();
        }
        chatList.add(chat);
        room.setChats(chatList); // 채팅 내역 업데이트
        chatroomRepository.save(room);

        // Response
        chatResponse.setRoomId(roomId);
        chatResponse.setMessageType(ChatResponse.MessageType.TALK);
        chatResponse.setUserName(nickname);
        chatResponse.setUserInfo(userInfo);
        chatResponse.setMessage(request.getMessage());
        chatResponse.setChatTime(time);

        //messagingTemplate.convertAndSend("sub/chat/room/" + request.getRoomId(), chatResponse);
        redisTemplate.convertAndSend(channelTopic.getTopic(), chatResponse);

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

    /* 채팅방 나가기 */
    public CheckResponse leaveChatroom(User user, Long roomId) {
        CheckResponse response = new CheckResponse();

        try {
            Integer userId = user.getUserId();
            Chatroom room = chatroomRepository.findByIdxAndUserListContaining(roomId, userId);

            String nickname = user.getNickname();
            String userYear = user.getStudentId().toString().substring(0, 2);
            String time = LocalDateTime.now().format(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            );
            String userInfo = user.getMajor() + " " + userYear;

            // DB
            Chat chat = new Chat();
            chat.setMessageType(Chat.MessageType.EXIT);
            chat.setUserId(userId);
            chat.setNickname(nickname);
            chat.setUserInfo(userInfo);
            chat.setMessage(nickname + " 님이 방에서 나왔습니다.");
            chat.setChatTime(time);
            room.setCurrentNumber(room.getCurrentNumber() - 1);
            List<Integer> newUserList = room.getUserList();
            newUserList.remove(userId);
            room.setUserList(newUserList);
            room.setLastChatTime(time); // 마지막 채팅 시간 업데이트
            List<Chat> chatList = room.getChats();
            if (chatList == null) {
                chatList = new ArrayList<>();
            }
            chatList.add(chat);
            room.setChats(chatList); // 채팅 내역 업데이트
            chatroomRepository.save(room);

            // STOMP
            ChatResponse chatResponse = new ChatResponse();
            chatResponse.setRoomId(roomId);
            chatResponse.setMessageType(ChatResponse.MessageType.EXIT);
            chatResponse.setUserName(nickname);
            chatResponse.setUserInfo(userInfo);
            chatResponse.setMessage(nickname + " 님이 방에서 나왔습니다.");
            chatResponse.setChatTime(time);
            redisTemplate.convertAndSend(channelTopic.getTopic(), chatResponse);

            response.setStatus(true);
            response.setMessage("[" + room.getRoomName() + "] 방에서 나왔습니다.");
            return response;

        } catch (NullPointerException e) {
            response.setStatus(false);
            response.setMessage("채팅방 혹은 사용자의 정보를 다시 확인해주세요.");
            return response;
        }

    }

    /* 채팅방 삭제 */
    public CheckResponse deleteChatroom(Integer userId, Long roomId) {
        CheckResponse response = new CheckResponse();

        try {
            Chatroom chatroom = chatroomRepository.findByIdx(roomId);

            if (chatroom.getHostUserId() != userId || chatroom.getHostUserId() == null) {
                response.setStatus(false);
                response.setMessage("방장이 아니면 채팅방을 삭제할 수 없습니다.");
                return response;
            }

            chatroomRepository.delete(chatroom);

            // redis 삭제는 좀 더 알아보고

            response.setStatus(true);
            response.setMessage("[" + chatroom.getRoomName() + "] 방을 삭제했습니다.");
            return response;

        } catch (NullPointerException e) {
            response.setStatus(false);
            response.setMessage("채팅방 혹은 사용자의 정보를 다시 확인해주세요.");
            return response;
        }
    }

    /* 채팅방의 채팅 목록 가져오기 */
    public List<ChatInfoMapping> getChatList (Long roomId) {
        return chatroomRepository.findAllByIdx(roomId);
    }


    /* 탈퇴한 사용자 -> DB userName을 '(알 수 없음)'으로 수정 */


}
