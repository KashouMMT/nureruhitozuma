package com.dev.main.controller.authentication;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dev.main.dto.UserDto;
import com.dev.main.service.UserService;
import com.dev.main.utils.LangPathUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/{lang}/auth")
public class AuthenticationController {
	
	private final UserService userService;
	
	public AuthenticationController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/login")
	public String loginPage(@RequestParam(required = false)String error, Model model) {
		if (error != null) model.addAttribute("loginError", true);
		model.addAttribute("info","login.login-info-noti");
		return "auth/login";
	}
	
	@GetMapping("/access-denied")
	public String accessDeniedPage() {
		return "auth/access-denied";
	}
	
	@GetMapping("/error")
	public String errorPage() {
		return "auth/error";
	}
	
	@GetMapping("/sign-up")
	public String signUpPage(Model model) {
		UserDto userDto = new UserDto();
		model.addAttribute("userDto",userDto);
		return "auth/sign-up";
	}
	
	@PostMapping("/register")
	public String registerNewAccount(@Valid @ModelAttribute UserDto userDto, BindingResult result,HttpServletRequest request, Model model) {
		
		String lang = LangPathUtils.resolveLangFromPath(request);
		
		if(result.hasErrors()) {
			model.addAttribute("userDto",userDto);
			return "auth/sign-up";
		}
		
		if(userService.isUsernameAlreadyExist(userDto.getEmail())) {
			model.addAttribute("userDto",userDto);
			model.addAttribute("warn","Username already exist. Please try again with different username.");
			
			return "auth/sign-up";
		}
		
		userService.createUser(userDto,false);
		
		return "redirect:/" + LangPathUtils.normalizeLang(lang) + "/auth/login";
	}
}
