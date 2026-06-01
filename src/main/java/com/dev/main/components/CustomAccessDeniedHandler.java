package com.dev.main.components;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.dev.main.utils.LangPathUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler{

	private static final Logger logger = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);
	
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
		
		String username = "anonymous";
		
		var auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) username = auth.getName();
		
		logger.warn("Unauthorized access attempt by user [{}] from IP [{}] to [{}]: {}", 
				username,
                request.getRemoteAddr(), 
                request.getRequestURI(), 
                accessDeniedException.getMessage());
		response.sendRedirect(LangPathUtils.buildLangUrl(request, "ja", "/home"));
	}
}
