//package com.dev.main.controller.user;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import com.dev.main.dto.ChatMessageDto;
//import com.dev.main.model.ChatMessage;
//import com.dev.main.security.MyUserDetails;
//import com.dev.main.service.ChatMessageService;
//import com.dev.main.service.UserService;
//import com.dev.main.utils.LangPathUtils;
//
//import jakarta.servlet.http.HttpServletRequest;
//
//@Controller
//@RequestMapping("/{lang}/user")
//public class UserController {
//	
//	private final UserService userService;
//	private final ChatMessageService messageService;
//
//	public UserController(UserService userService, ChatMessageService messageService) {
//		super();
//		this.userService = userService;
//		this.messageService = messageService;
//	}
//
//	@GetMapping("/profile")
//	public String profilePage(@AuthenticationPrincipal MyUserDetails userDetails,
//		Model model) {
//		
//		String userId = userDetails.getUsername();
//		
//		List<ChatMessage> messages = messageService.getLatestChatsForUser(userId);
//		List<ChatMessageDto> messagesDtos = new ArrayList<>();
//		for(ChatMessage message : messages) {
//			ChatMessageDto messageDto = new ChatMessageDto();
//			messageDto.setSenderId(message.getSenderId());
//			messageDto.setReceiverId(message.getReceiverId());
//			messageDto.setRoomId(message.getRoomId());
//			messageDto.setContent(message.getContent());
//			messageDto.setSentAt(message.getSentAt());
//			messageDto.setStatus(message.getStatus().name());
//			messagesDtos.add(messageDto);
//		}
//		
//		model.addAttribute("chats",messagesDtos);
//		model.addAttribute("userId",userId);
//		model.addAttribute("content","public/content/profile");
//		
//		return "public/public-layout";
//	}
//	
//	@PostMapping("/profile")
//	public String changeUserInfo(@RequestParam(required = false) String username,
//		@RequestParam(required = false) String oldPassword,
//		@RequestParam(required = false) String newPassword,
//		@AuthenticationPrincipal MyUserDetails userDetails,RedirectAttributes ra,
//		HttpServletRequest request,
//		Model model) {
//		
//		String lang = LangPathUtils.resolveLangFromPath(request);
//		
//		if(userService.getUserByEmail(username) != null) ra.addFlashAttribute("error","profile.error-noti");
//		
//		if(userService.editUserInfo(userDetails.getId(), username, oldPassword, newPassword)) 
//			ra.addFlashAttribute("success","profile.success-noti");
//		else ra.addFlashAttribute("warn","profile.warn-noti");
//		
//		return "redirect:" + LangPathUtils.buildLangUrl(request, lang, "/user/profile");
//	}
//	
//	@GetMapping("/profile/{roomId}")
//	public String chatPage(@PathVariable String roomId,
//		@AuthenticationPrincipal MyUserDetails userDetails,
//		HttpServletRequest request,
//		Model model) {
//		
//		String userId = userDetails.getUsername();
//		
//		String[] parts = roomId.split("_");
//
//		String userIdFromRoom = null;
//		if (parts.length == 2) {
//			userIdFromRoom = parts[1];
//		}
//		
//		if(!userId.equals(userIdFromRoom)) return "redirect:" + LangPathUtils.buildLangUrl(request, "/auth/access-denied");
//		
//		messageService.setMessagesAsRead(roomId, userId);
//		
//		List<ChatMessage> messages = messageService.getLatestChatsForUser(userId);
//		List<ChatMessageDto> messagesDtos = new ArrayList<>();
//		for(ChatMessage message : messages) {
//			ChatMessageDto messageDto = new ChatMessageDto();
//			messageDto.setSenderId(message.getSenderId());
//			messageDto.setReceiverId(message.getReceiverId());
//			messageDto.setRoomId(message.getRoomId());
//			messageDto.setContent(message.getContent());
//			messageDto.setSentAt(message.getSentAt());
//			messageDto.setStatus(message.getStatus().name());
//			parts = message.getRoomId().split("_");
//			messagesDtos.add(messageDto);
//		}
//		
//		model.addAttribute("chats",messagesDtos);
//		model.addAttribute("messages",messageService.getMessagesByRoomId(roomId));
//		model.addAttribute("currentRoomId",roomId);
//		model.addAttribute("currentUserId",userId);
//		model.addAttribute("userId",userId);
//		model.addAttribute("receiverId","admin");
//		model.addAttribute("content","public/content/profile");
//		
//		return "public/public-layout";
//	}
//}
