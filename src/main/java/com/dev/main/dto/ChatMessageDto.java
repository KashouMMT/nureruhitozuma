package com.dev.main.dto;

import java.time.Instant;

public class ChatMessageDto {
	private String roomId;
    private String senderId;
    private String receiverId;
    private String content;
    private Instant sentAt;
    private String status;
    private String image;
	
    public ChatMessageDto() {
    	
    }

	public ChatMessageDto(String roomId, String senderId, String receiverId, String content, Instant sentAt,
			String status, String image) {
		super();
		this.roomId = roomId;
		this.senderId = senderId;
		this.receiverId = receiverId;
		this.content = content;
		this.sentAt = sentAt;
		this.status = status;
		this.image = image;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	public String getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Instant getSentAt() {
		return sentAt;
	}

	public void setSentAt(Instant sentAt) {
		this.sentAt = sentAt;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
