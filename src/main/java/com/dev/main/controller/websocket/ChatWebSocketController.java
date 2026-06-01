//package com.dev.main.controller.websocket;
//
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Controller;
//
//import com.dev.main.dto.ChatMessageDto;
//import com.dev.main.model.ChatMessage;
//import com.dev.main.service.ChatMessageService;
//import com.dev.main.service.LineMessageService;
//
//@Controller
//public class ChatWebSocketController {
//
//	private final SimpMessagingTemplate messagingTemplate;
//	private final ChatMessageService messageService;
//	private final LineMessageService lineMessageService;
//
//	public ChatWebSocketController(SimpMessagingTemplate messagingTemplate, ChatMessageService messageService,
//			LineMessageService lineMessageService) {
//		super();
//		this.messagingTemplate = messagingTemplate;
//		this.messageService = messageService;
//		this.lineMessageService = lineMessageService;
//	}
//
//	@MessageMapping("/chat.send")
//	public void ProcessMessage(@Payload ChatMessageDto messageDto) {
//		
//		ChatMessage saved = messageService.save(messageDto);
//		String destination = "/topic/chat." + saved.getRoomId();
//		messagingTemplate.convertAndSend(destination,saved);
//		if(messageDto.getReceiverId().equals("admin")) lineMessageService.sendInquiry(messageDto.getSenderId(),messageDto.getContent());
//	}
//}
