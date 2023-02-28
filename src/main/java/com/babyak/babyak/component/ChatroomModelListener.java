//package com.babyak.babyak.component;
//
//import com.babyak.babyak.domain.chat.Chatroom;
//import com.babyak.babyak.service.SequenceGeneratorService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
//import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
//import org.springframework.stereotype.Component;
//
//@RequiredArgsConstructor
//@Component
//public class ChatroomModelListener extends AbstractMongoEventListener<Chatroom> {
//
//    private final SequenceGeneratorService sequenceGenerator;
//
//    @Override
//    public void onBeforeConvert(BeforeConvertEvent<Chatroom> event) {
//        event
//                .getSource()
//                .setId(sequenceGenerator.generateSequence(Chatroom.SEQUENCE_NAME));
//    }
//}
