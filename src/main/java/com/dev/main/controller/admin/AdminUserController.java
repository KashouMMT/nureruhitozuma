package com.dev.main.controller.admin;

import java.util.Locale;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dev.main.security.MyUserDetails;
import com.dev.main.service.UserService;
import com.dev.main.utils.LangPathUtils;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/{lang}/admin")
public class AdminUserController {

	private final UserService userService;

	public AdminUserController(UserService userService) {
		super();
		this.userService = userService;
	}

	@GetMapping("/user")
	public String userListPage(@AuthenticationPrincipal MyUserDetails userDetails,Locale locale,Model model) {
		
		String username = userDetails.getUsername();
		model.addAttribute("users",userService.getAllUsers().stream()
				.filter(user -> 
					user.getRoles().stream()
						.anyMatch(role -> 
							"ROLE_USER".equals(role.getRoleName()))).toList());
		model.addAttribute("username",username);
		model.addAttribute("isAdmin",false);
		model.addAttribute("content","admin/content/admin-users-tables");
				
		return "admin/admin-layout";
	}
	
	@GetMapping("/add-user-why-not-just-redirect")
	public String addUserPage(HttpServletRequest request,Model model) {
		return "redirect:" + LangPathUtils.buildLangUrl(request,"/auth/sign-up");
	}
	
	@GetMapping("/disable-user/{id}")
	public String disableUser(HttpServletRequest request,@PathVariable Long id) {
		userService.disableUser(id);
		return "redirect:" + LangPathUtils.buildLangUrl(request, "/admin/user");
	}
	
	@GetMapping("/enable-user/{id}")
	public String enableUser(HttpServletRequest request,@PathVariable Long id) {
		userService.enableUser(id);
		return "redirect:" + LangPathUtils.buildLangUrl(request, "/admin/user");
	}
}
