package com.dev.main.controller.admin;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;

import com.dev.main.security.MyUserDetails;
import com.dev.main.utils.LangPathUtils;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/{lang}/admin")
public class AdminDashboardController {

	@GetMapping({"","/",})
	public String redirectToAdminDashboard(HttpServletRequest request) {
		return "redirect:" + LangPathUtils.buildLangUrl(request,"/admin/dashboard");
	}
	
	@GetMapping("/dashboard")
	public String adminDashboardPage(@AuthenticationPrincipal MyUserDetails userDetails,Model model,SessionStatus status) {
		
		status.setComplete();
		
		String username = userDetails.getUsername();
		model.addAttribute("username",username);
		
		model.addAttribute("content","admin/content/admin-dashboard");
		
		return "admin/admin-layout";
	}
	
	@GetMapping("/test")
	public String adminTestPage(Model model) {
		
		model.addAttribute("content","admin/content/admin-static");
		
		return "admin/admin-layout";
	}
}
