package com.dev.main.components;

import java.io.IOException;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.dev.main.security.MyUserDetails;
import com.dev.main.utils.LangPathUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler{

	private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		MyUserDetails userDetails = ((MyUserDetails) authentication.getPrincipal());
		
//		String langParam = request.getParameter("lang");
//		String lang = LangPathUtils.normalizeLang(langParam);
//		if(lang == null) lang = LangPathUtils.resolveLangFromPath(request);
		
		Set<String> authorities = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
		
		if(authorities.contains("ROLE_ADMIN")) {
			logger.info("Login Success: IP [{}], Email: [{}] with [ROLE_ADMIN] at: {}",
					request.getRemoteAddr(),
					userDetails.getUsername(),
					new java.util.Date());
			
			if(userDetails.getUsername().equals("admin925")) response.sendRedirect(LangPathUtils.buildLangUrl(request, "ja", "/admin/profile"));
			else response.sendRedirect(LangPathUtils.buildLangUrl(request, "ja", "/admin/dashboard"));
		
		} else if(authorities.contains("ROLE_USER")) {
			logger.info("Login Success: IP [{}], Email: [{}] with [ROLE_USER] at: {}",
					request.getRemoteAddr(),
					userDetails.getUsername(),
					new java.util.Date());
			
			response.sendRedirect(LangPathUtils.buildLangUrl(request, "ja", "/home"));
		} else {
			logger.warn("Login Success: IP [{}], Email: [{}] with [UNKNOWN ROLE] at: {}",
					request.getRemoteAddr(),
					userDetails.getUsername(),
					new java.util.Date());
			
			response.sendRedirect(LangPathUtils.buildLangUrl(request, "ja", "/home"));
		}
	}

}
