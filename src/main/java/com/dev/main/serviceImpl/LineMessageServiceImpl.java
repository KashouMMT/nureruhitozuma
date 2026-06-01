//package com.dev.main.serviceImpl;
//
//import java.util.Arrays;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import com.dev.main.service.LineMessageService;
//import com.linecorp.bot.client.LineMessagingClient;
//import com.linecorp.bot.model.PushMessage;
//import com.linecorp.bot.model.message.TextMessage;
//
//@Service
//public class LineMessageServiceImpl implements LineMessageService{
//
//    private final LineMessagingClient client;
//    private final List<String> toIds;
//
//	public LineMessageServiceImpl(LineMessagingClient client,
//                              @Value("${line.bot.user-ids}") String toIdProperty) {
//        this.client = client;
//        this.toIds = Arrays.stream(toIdProperty.split(","))
//        			.map(String::trim)
//        			.filter(s -> !s.isEmpty())
//        			.toList();
//    }
//
//	@Override
//    public void sendInquiry(String name) {
//	    String text =
//	            "新しいお問い合わせ\n" +
//	            "ユーザー名：" + name;
//	    
//	    TextMessage message = new TextMessage(text);
//	    
//	    for(String toId : toIds) {
//	    	client.pushMessage(new PushMessage(toId, message));
//	    }
//    }
//
//	@Override
//	public void sendInquiry(String name, String message) {
//	    String text =
//	            "新しいお問い合わせ\n" +
//	            "ユーザー名：" + name + 
//	            "メッセージ" + message;
//	    
//	    TextMessage messageToLine = new TextMessage(text);
//	    
//	    for(String toId : toIds) {
//	    	client.pushMessage(new PushMessage(toId, messageToLine));
//	    }
//	}
//	
////	@Override
////    public void sendInquiry(String name) {
////		return;
////    }
////
////	@Override
////	public void sendInquiry(String name, String message) {
////		return;
////	}
//}
