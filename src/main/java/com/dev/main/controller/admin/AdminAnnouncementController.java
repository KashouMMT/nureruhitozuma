package com.dev.main.controller.admin;

import java.util.Locale;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dev.main.dto.AnnouncementDto;
import com.dev.main.model.Announcement;
import com.dev.main.security.MyUserDetails;
import com.dev.main.service.AnnouncementService;
import com.dev.main.service.UserService;
import com.dev.main.utils.LangPathUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/{lang}/admin")
public class AdminAnnouncementController {

	private final AnnouncementService announcementService;
	private final UserService userService;
	
	public AdminAnnouncementController(AnnouncementService announcementService,UserService userService) {
		super();
		this.announcementService = announcementService;
		this.userService = userService;
	}
	
	@GetMapping("/announcement")
	public String getAllAnnouncements(Model model) {
		
		model.addAttribute("content","admin/content/admin-announcement");
		model.addAttribute("announcements",announcementService.getAllAnnouncements());
		
		return "admin/admin-layout";
	}
	
	@GetMapping("/add-announcement")
	public String getAddAnnoumcementPage(Locale locale,Model model) {
		
		AnnouncementDto announcementDto = new AnnouncementDto();
		
		model.addAttribute("objectDto",announcementDto);
		model.addAttribute("formAction","/%s/admin/add-announcement".formatted(locale.getLanguage()));
		model.addAttribute("content","admin/content/admin-announcement-form");
		
		return "admin/admin-layout";
	}
	
	@PostMapping("/add-announcement")
	public String postAddAnnouncementPage(@Valid @ModelAttribute("objectDto") AnnouncementDto announcementDto,
			BindingResult result,@AuthenticationPrincipal MyUserDetails userDetails ,HttpServletRequest request, Model model) {
		
		String lang = LangPathUtils.resolveLangFromPath(request);
		
		if(result.hasErrors()) {
			model.addAttribute("objectDto",announcementDto);
			model.addAttribute("formAction","/%s/admin/add-announcement".formatted(lang));
			model.addAttribute("content","admin/content/admin-announcement-form");
			
			return "admin/admin-layout";
		}
		
		announcementService.createAnnouncement(announcementDto,userService.getUserById(userDetails.getId()));
		
		return "redirect:" + LangPathUtils.buildLangUrl(request, "/admin/announcement");
	}
	
	@GetMapping("/edit-announcement/{id}")
	public String getEditAnnouncement(@PathVariable Long id,Locale locale,Model model) {
		Announcement announcement = announcementService.getAnnouncementById(id);
		AnnouncementDto announcementDto = new AnnouncementDto();
		
		announcementDto.setTitle(announcement.getTitle());
		announcementDto.setContent(announcement.getContent());
		
		model.addAttribute("objectDto",announcementDto);
		model.addAttribute("announcement",announcementService.getAnnouncementById(id));
		model.addAttribute("formAction","/%s/admin/edit-announcement/%d".formatted(locale.getLanguage(),id));
		model.addAttribute("formTitle","admin.announcement.edit");
		model.addAttribute("content","admin/content/admin-announcement-form");
		
		return "admin/admin-layout";
	}
	
	@PostMapping("/edit-announcement/{id}")
	public String postEditAnnouncement(@Valid @ModelAttribute("objectDto") AnnouncementDto announcementDto,
		BindingResult result,@PathVariable Long id, @AuthenticationPrincipal MyUserDetails userDetails, 
		HttpServletRequest request,Model model) {
		
		String lang = LangPathUtils.resolveLangFromPath(request);
		
		if(result.hasErrors()) {
			model.addAttribute("objectDto",announcementDto);
			model.addAttribute("announcement",announcementService.getAnnouncementById(id));
			model.addAttribute("formAction","/%s/admin/edit-announcement/%d".formatted(lang,id));
			model.addAttribute("formTitle","admin.announcement.edit");
			model.addAttribute("content","admin/content/admin-announcement-form");
			
			return "admin/admin-layout";
		}
		
		announcementService.editAnnouncement(id, announcementDto, userService.getUserById(userDetails.getId()));
		
		return "redirect:" + LangPathUtils.buildLangUrl(request, "/admin/announcement");
	}
	
	@GetMapping("/delete-announcement/{id}")
	public String deleteAnnouncement(@PathVariable Long id,HttpServletRequest request) {
		announcementService.deleteAnnouncement(id);
		
		return "redirect:" + LangPathUtils.buildLangUrl(request, "/admin/announcement");
	}
}
