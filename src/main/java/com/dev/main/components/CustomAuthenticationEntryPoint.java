package com.dev.main.components;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.dev.main.utils.LangPathUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint{

	private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationEntryPoint.class);
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		logger.warn("Unauthorized access attempt by anonymous user from IP [{}] to [{}]: {}", 
                request.getRemoteAddr(), 
                request.getRequestURI(), 
                authException.getMessage());
		String target = LangPathUtils.buildLangUrl(request, "ja", "/auth/login");
		response.sendRedirect(target);
	}
}
