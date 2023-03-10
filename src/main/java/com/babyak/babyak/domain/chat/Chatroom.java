package com.babyak.babyak.domain.chat;

import com.babyak.babyak.domain.chat.Chat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.List;

@Document(collection = "chatrooms")
@Getter
@Setter
@NoArgsConstructor
public class Chatroom {

    @Transient
    public static final String SEQUENCE_NAME = "chatroom_sequence";

    @Id
    private String id;
    private Long idx; // 채팅방 번호
    private String roomName; // 채팅방 이름
    private String description; // 채팅방 설명
    private Integer hostUserId;
    private String hostUserName;
    private String hostUserInfo;
    private int maxPeople; // 최대 인원
    private int currentNumber; // 현재 참여 인원
    private String lastChatTime; // 마지막 채팅 타임
    private List<Integer> userList; // 채팅방 참여 유저 정보 (userId)
    private List<Chat> chats; // 채팅 정보

    public Chatroom(String roomName, String description, int maxPeople) {
        this.roomName = roomName;
        this.description = description;
        this.maxPeople = maxPeople;
    }

    public Chatroom(
            String roomName, String description, int currentNumber, List<Integer> userList, List<Chat> chats
    ) {
        this.roomName = roomName;
        this.description = description;
        this.currentNumber = currentNumber;
        this.userList = userList;
        this.chats = chats;
    }


}
