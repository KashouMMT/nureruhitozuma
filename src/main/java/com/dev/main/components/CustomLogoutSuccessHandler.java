package com.dev.main.components;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.FlashMapManager;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.support.SessionFlashMapManager;

import com.dev.main.security.MyUserDetails;
import com.dev.main.utils.LangPathUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler{

	private static final Logger logger = LoggerFactory.getLogger(CustomLogoutSuccessHandler.class);
	
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		
//		String langParam = request.getParameter("lang");
//		String lang = LangPathUtils.normalizeLang(langParam);
//		if(lang == null) lang = LangPathUtils.resolveLangFromPath(request);
		
		MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
		String username = (userDetails != null ? userDetails.getUsername() : "anonymous");
		
		logger.info("User logged out from IP [{}], username={}",request.getRemoteAddr(),username);
		
		FlashMap flashMap = new FlashMap();
		flashMap.put("success", "login.logout-success-noti");
		
        FlashMapManager flashMapManager = RequestContextUtils.getFlashMapManager(request);
        if (flashMapManager == null) flashMapManager = new SessionFlashMapManager();
        flashMapManager.saveOutputFlashMap(flashMap, request, response);
        
        String target = LangPathUtils.buildLangUrl(request, "ja","/auth/login");
        response.sendRedirect(target);
	}
}
