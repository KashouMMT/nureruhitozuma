package com.dev.main.components;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.FlashMapManager;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.support.SessionFlashMapManager;

import com.dev.main.utils.LangPathUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

	private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		
//		String langParam = request.getParameter("lang");
//		String lang = LangPathUtils.normalizeLang(langParam);
//		if(langParam == null) lang = LangPathUtils.resolveLangFromPath(request);
		
		request.getSession().setAttribute("errorMessage", exception);
		logger.warn("Login failed for IP [{}]: {}", 
				request.getRemoteAddr(), 
				exception.getMessage());
		
		FlashMap flashMap = new FlashMap();
		flashMap.put("warn", "login.login-fail-noti");
		FlashMapManager flashMapManager = RequestContextUtils.getFlashMapManager(request);
		if(flashMapManager == null) flashMapManager = new SessionFlashMapManager();
		flashMapManager.saveOutputFlashMap(flashMap, request, response);
		String target = LangPathUtils.buildLangUrl(request, "ja", "/auth/login");
		response.sendRedirect(target);
	}
}
