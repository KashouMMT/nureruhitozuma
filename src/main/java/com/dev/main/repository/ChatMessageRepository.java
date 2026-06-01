//package com.dev.main.repository;
//
//import java.util.List;
//
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.mongodb.repository.Aggregation;
//import org.springframework.data.mongodb.repository.MongoRepository;
//import org.springframework.data.mongodb.repository.Query;
//import org.springframework.data.mongodb.repository.Update;
//import org.springframework.stereotype.Repository;
//
//import com.dev.main.model.ChatMessage;
//
//@Repository
//public interface ChatMessageRepository extends MongoRepository<ChatMessage, String>{
//	List<ChatMessage> findByRoomIdOrderBySentAtAsc(String roomId);
//	List<ChatMessage> findTop50ByRoomIdOrderBySentAtDesc(String roomId);
//	
//    @Aggregation(pipeline = {
//            "{ $match: { $or: [ { 'senderId': ?0 }, { 'receiverId': ?0 } ] } }",
//            "{ $sort: { 'sentAt': -1 } }",
//            "{ $group: { _id: '$roomId', latest: { $first: '$$ROOT' } } }",
//            "{ $replaceRoot: { newRoot: '$latest' } }",
//            "{ $sort: { 'sentAt': -1, 'roomId' : 1 } }"
//    })
//	List<ChatMessage> findLatestMessagePerRoomForUser(String userId);
//    
//    @Modifying
//    @Query(value = "{ 'roomId': ?0, 'receiverId': ?1, 'status': 'UNREAD'}")
//    @Update("{ '$set': { 'status' : 'READ' } }")
//    void markMessageAsRead(String roomId, String receiverId);
//    
//    boolean existsByRoomId(String roomId);
//}
