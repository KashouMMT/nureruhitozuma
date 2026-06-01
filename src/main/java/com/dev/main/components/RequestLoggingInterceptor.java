package com.dev.main.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {
	
	private static final Logger logger = LoggerFactory.getLogger(RequestLoggingInterceptor.class);
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		
		String uri = request.getRequestURI();
		String method = request.getMethod();
		String ip = request.getRemoteAddr();
		
		String username = "anonymous";
		var auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
			username = auth.getName();
		}
		logger.info("Request: [{}] {} from IP [{}] by user [{}]", 
				method, 
				uri, 
				ip, 
				username);
		
		return true;
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable Exception ex) throws Exception {
		
		int status = response.getStatus();
		
		if(status >= 300 && status <= 400) {
			String uri = request.getRequestURI();
			String method = request.getMethod();
			String ip = request.getRemoteAddr();
			String username = "anonymous";
			
			var auth = SecurityContextHolder.getContext().getAuthentication();
	        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
	            username = auth.getName();
	        }

	        String location = response.getHeader("Location");

	        logger.info(
	            "Redirect: [{}] {} from IP [{}] by user [{}] -> [{}] (status={})",
	            method,
	            uri,
	            ip,
	            username,
	            location,
	            status
	        );
		}
	}
}
