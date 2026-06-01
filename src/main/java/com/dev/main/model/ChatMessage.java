//package com.dev.main.model;
//
//import java.time.Instant;
//
//import org.springframework.data.mongodb.core.index.Indexed;
//import org.springframework.data.mongodb.core.mapping.Document;
//
//import com.dev.main.data.MessageStatus;
//
//import jakarta.persistence.Id;
//
//@Document(collection = "chat_messages")
//public class ChatMessage {
//	
//	@Id
//	private String id;
//	
//	@Indexed
//	private String roomId;
//	
//	private String senderId;
//	private String receiverId;
//	private String content;
//	
//	@Indexed
//	private Instant sentAt = Instant.now();
//	
//	@Indexed
//	private MessageStatus status = MessageStatus.UNREAD;
//
//	public ChatMessage(String id, String roomId, String senderId, String receiverId, String content, Instant sentAt,
//			MessageStatus status) {
//		super();
//		this.id = id;
//		this.roomId = roomId;
//		this.senderId = senderId;
//		this.receiverId = receiverId;
//		this.content = content;
//		this.sentAt = sentAt;
//		this.status = status;
//	}
//	
//	public ChatMessage() {
//		
//	}
//
//	public String getId() {
//		return id;
//	}
//
//	public void setId(String id) {
//		this.id = id;
//	}
//
//	public String getRoomId() {
//		return roomId;
//	}
//
//	public void setRoomId(String roomId) {
//		this.roomId = roomId;
//	}
//
//	public String getSenderId() {
//		return senderId;
//	}
//
//	public void setSenderId(String senderId) {
//		this.senderId = senderId;
//	}
//
//	public String getReceiverId() {
//		return receiverId;
//	}
//
//	public void setReceiverId(String receiverId) {
//		this.receiverId = receiverId;
//	}
//
//	public String getContent() {
//		return content;
//	}
//
//	public void setContent(String content) {
//		this.content = content;
//	}
//
//	public Instant getSentAt() {
//		return sentAt;
//	}
//
//	public void setSentAt(Instant sentAt) {
//		this.sentAt = sentAt;
//	}
//
//	public MessageStatus getStatus() {
//		return status;
//	}
//
//	public void setStatus(MessageStatus status) {
//		this.status = status;
//	}
//}
