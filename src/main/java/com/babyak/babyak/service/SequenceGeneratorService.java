//package com.babyak.babyak.service;
//
//import com.babyak.babyak.domain.chat.DatabaseSequence;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.mongodb.core.MongoOperations;
//import org.springframework.data.mongodb.core.ReactiveMongoOperations;
//import org.springframework.data.mongodb.core.query.Update;
//import org.springframework.stereotype.Service;
//import reactor.core.publisher.Mono;
//
//import java.util.Objects;
//import java.util.concurrent.atomic.AtomicReference;
//
//import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
//import static org.springframework.data.mongodb.core.query.Criteria.where;
//import static org.springframework.data.mongodb.core.query.Query.query;
//
//@Service
//@RequiredArgsConstructor
//public class SequenceGeneratorService {
//
//    //private final ReactiveMongoOperations mongoOperations;
//    private final MongoOperations mongoOperations;
//
//    public Long generateSequence(String seqName) {
//        DatabaseSequence counter = mongoOperations.findAndModify(
//                query(where("_id").is(seqName)),
//                new Update().inc("seq", 1), options().returnNew(true).upsert(true),
//                DatabaseSequence.class);
//
//        return !Objects.isNull(counter) ? counter.getSeq() : 1;
//    }
//}
//
///*
//    private final MongoOperations mongoOperations;
//
//    public Long generateSequence(String seqName) {
//        DatabaseSequence counter = mongoOperations.findAndModify(
//                query(where("_id").is(seqName)),
//                new Update().inc("seq", 1), options().returnNew(true).upsert(true),
//                DatabaseSequence.class);
//
//        return !Objects.isNull(counter) ? counter.getSeq() : 1;
//    }
// */