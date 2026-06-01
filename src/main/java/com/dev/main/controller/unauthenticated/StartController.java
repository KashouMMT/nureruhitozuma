package com.dev.main.controller.unauthenticated;

import java.util.Locale;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.dev.main.security.MyUserDetails;
import com.dev.main.utils.LangPathUtils;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class StartController {

	@GetMapping({"","/","/home","/admin"})
	public String redirectStartController(@AuthenticationPrincipal MyUserDetails userDetails,
			HttpServletRequest request,Locale locale) {
		
		if(userDetails != null && userDetails.isAdmin()) 
			return "redirect:" + LangPathUtils.buildLangUrl(request, "/admin/dashboard");
		
		return "redirect:" + LangPathUtils.buildLangUrl(request, "/home");
	}
}
