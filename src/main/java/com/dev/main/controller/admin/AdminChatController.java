//package com.dev.main.controller.admin;
//
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import com.dev.main.security.MyUserDetails;
//import com.dev.main.service.ChatMessageService;
//
//@Controller
//@RequestMapping("/{lang}/admin")
//public class AdminChatController {
//	
//	private final ChatMessageService messageService;
//
//	public AdminChatController(ChatMessageService messageService) {
//		super();
//		this.messageService = messageService;
//	}
//
//	@GetMapping("/chat")
//	public String chatPage(@AuthenticationPrincipal MyUserDetails userDetails,
//		Model model) {
//		
//		model.addAttribute("chats",messageService.getLatestChatsForUser("admin"));
//		model.addAttribute("content","admin/content/admin-chat");
//		
//		return "admin/admin-layout";
//	}
//	
//	@GetMapping("/chat/{roomId}")
//	public String chatPage(@PathVariable String roomId,
//		@AuthenticationPrincipal MyUserDetails userDetails, Model model) {
//		
//		// roomId = room_user123
//		String[] parts = roomId.split("_");
//
//		String receiverId = null;
//		if (parts.length == 2) {
//			receiverId = parts[1];
//		}
//		
//		messageService.setMessagesAsRead(roomId, "admin");
//		model.addAttribute("chats",messageService.getLatestChatsForUser("admin"));
//		model.addAttribute("messages",messageService.getMessagesByRoomId(roomId));
//		model.addAttribute("currentRoomId",roomId);
//		model.addAttribute("currentUserId","admin");
//		model.addAttribute("receiverId",receiverId);
//		model.addAttribute("content","admin/content/admin-chat");
//		
//		return "admin/admin-layout";
//	}
//}
