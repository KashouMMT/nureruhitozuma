//package com.dev.main.serviceImpl;
//
//import java.time.Instant;
//import java.util.List;
//
//import org.springframework.stereotype.Service;
//
//import com.dev.main.data.MessageStatus;
//import com.dev.main.dto.ChatMessageDto;
//import com.dev.main.model.ChatMessage;
//import com.dev.main.repository.ChatMessageRepository;
//import com.dev.main.service.ChatMessageService;
//
//@Service
//public class ChatMessageServiceImpl implements ChatMessageService{
//
//	private final ChatMessageRepository chatMessageRepo;
//
//	public ChatMessageServiceImpl(ChatMessageRepository chatMessageRepo) {
//		super();
//		this.chatMessageRepo = chatMessageRepo;
//	}
//
//	@Override
//	public ChatMessage save(ChatMessageDto chatMessageDto) {
//		ChatMessage message = new ChatMessage();
//		message.setRoomId(chatMessageDto.getRoomId());
//		message.setReceiverId(chatMessageDto.getReceiverId());
//		message.setSenderId(chatMessageDto.getSenderId());
//		message.setContent(chatMessageDto.getContent());
//		message.setStatus(MessageStatus.UNREAD);
//		message.setSentAt(Instant.now());
//		return chatMessageRepo.save(message);
//	}
//
//	@Override
//	public List<ChatMessage> getMessagesByRoomId(String roomId) {
//		return chatMessageRepo.findByRoomIdOrderBySentAtAsc(roomId);
//	}
//
//	@Override
//	public List<ChatMessage> getLatestChatsForUser(String userId) {
//		return chatMessageRepo.findLatestMessagePerRoomForUser(userId);
//	}
//
//	@Override
//	public void setMessagesAsRead(String roomId, String receiverId) {
//		chatMessageRepo.markMessageAsRead(roomId, receiverId);
//	}
//
//	@Override
//	public boolean isRoomIdExist(String roomId) {
//		return chatMessageRepo.existsByRoomId(roomId);
//	}
//}
