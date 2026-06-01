package com.dev.main.controller.admin;

import java.util.Locale;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dev.main.dto.UserDto;
import com.dev.main.security.MyUserDetails;
import com.dev.main.service.UserService;
import com.dev.main.utils.LangPathUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/{lang}/admin")
public class AdminController {
	
	private final UserService userService;

	public AdminController(UserService userService) {
		super();
		this.userService = userService;
	}

	@GetMapping("/user-admin")
	public String adminListController(@AuthenticationPrincipal MyUserDetails userDetails,Model model) {
		
		String username = userDetails.getUsername();
		
		model.addAttribute("users",userService.getAllUsers().stream()
			.filter(user -> 
				user.getRoles().stream()
					.anyMatch(role -> 
						"ROLE_ADMIN".equals(role.getRoleName()))).toList());
		model.addAttribute("username",username);
		model.addAttribute("isAdmin",true);
		model.addAttribute("content","admin/content/admin-users-tables");
		
		return "admin/admin-layout";
	}
	
	@GetMapping("/add-admin")
	public String addAdminPage(Locale locale,Model model) {
		
		UserDto userDto = new UserDto();
		
		model.addAttribute("objectDto",userDto);
		model.addAttribute("formAction","/%s/admin/add-admin".formatted(locale.getLanguage()));
		model.addAttribute("content","admin/content/admin-user-form");
		
		return "admin/admin-layout";
	}
	
	@PostMapping("/add-admin")
	public String postAddAdminPage(@Valid @ModelAttribute("objectDto")UserDto userDto,
		BindingResult result,HttpServletRequest request, Model model) {
		
		String lang = LangPathUtils.resolveLangFromPath(request);
		
		if(result.hasErrors()) {
			model.addAttribute("objectDto",userDto);
			model.addAttribute("formAction","/%s/admin/add-admin".formatted(lang));
			model.addAttribute("content","admin/content/admin-user-form");
			
			return "admin/admin-layout";
		}
		userService.createUser(userDto, true);
		
		return "redirect:" + LangPathUtils.buildLangUrl(request, lang,"/admin/user-admin");
	}
	
	@GetMapping("/profile")
	public String adminProfilePage(@AuthenticationPrincipal MyUserDetails userDetails,Locale locale,
		Model model) {
		
		UserDto userDto = new UserDto();
		userDto.setEmail(userDetails.getUsername());
		
		model.addAttribute("objectDto",userDto);
		
		if("admin925".equals(userDetails.getUsername())) model.addAttribute("danger","admin.profile-danger-noti");
		model.addAttribute("formAction","/%s/admin/profile".formatted(locale.getLanguage()));
		model.addAttribute("formTitle","admin.profile-edit");
		model.addAttribute("content","admin/content/admin-user-form");
		
		return "admin/admin-layout";
	}
	
	@PostMapping("/profile")
	public String postAdminProfilePage(@Valid @ModelAttribute("objectDto")UserDto userDto,BindingResult result, 
		@AuthenticationPrincipal MyUserDetails userDetails, HttpServletRequest request,
		RedirectAttributes ra, Model model) {
		
		String lang = LangPathUtils.resolveLangFromPath(request);
		
		if(result.hasErrors()) {
			model.addAttribute("formAction","/%s/admin/profile".formatted(lang));
			model.addAttribute("formTitle","admin.profile-edit");
			model.addAttribute("content","admin/content/admin-user-form");
			
			return "admin/admin-layout";
		}
		
		if("admin925".equals(userDetails.getUsername())) model.addAttribute("danger","admin.profile-danger-noti");
		
		if(userService.editUserInfo(userDetails.getId(), userDto.getEmail(), userDto.getOldPassword(), userDto.getPassword())) 
			ra.addFlashAttribute("success","admin.profile-success-noti");
		else ra.addFlashAttribute("warn","admin.profile-warn-noti");
		
		return "redirect:" + LangPathUtils.buildLangUrl(request, lang,"/admin/profile");
	}
}
