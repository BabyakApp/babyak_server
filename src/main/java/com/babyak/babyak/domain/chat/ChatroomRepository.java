package com.babyak.babyak.domain.chat;

import com.babyak.babyak.domain.chat.Chatroom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatroomRepository extends MongoRepository<Chatroom, String> {

    public List<Chatroom> findAll();

    public Chatroom findByIdx(Long idx);

    public List<Chatroom> findChatroomsByUserListContaining(Integer userId);

    public Chatroom findByIdxAndUserListContaining(Long idx, Integer userId);

}
