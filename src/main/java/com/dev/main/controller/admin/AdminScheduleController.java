package com.dev.main.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dev.main.service.ProductService;
import com.dev.main.service.ScheduleService;
import com.dev.main.utils.LangPathUtils;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/{lang}/admin")
public class AdminScheduleController {
	
	private final ScheduleService scheduleService;
	private final ProductService productService;

	public AdminScheduleController(ScheduleService scheduleService,ProductService productService) {
		super();
		this.scheduleService = scheduleService;
		this.productService = productService;
	}

	@GetMapping("/schedule")
	public String getSchedulePage(Model model) {
		
		model.addAttribute("content","admin/content/admin-schedule");
		model.addAttribute("products",productService.getAllProducts());
		
		return "admin/admin-layout";
	}
	
	@GetMapping("/edit-schedule/{productId}")
	public String getCalendarPage(@PathVariable Long productId, 
		Model model) {
		
		model.addAttribute("content","admin/content/admin-schedule-calander");
		model.addAttribute("productId",productId);
		model.addAttribute("selectedDates",scheduleService.getAllSchedulesByProductIdAndReturnAsString(productId));
		
		return "admin/admin-layout";
	}

	@PostMapping("/edit-schedule/{productId}")
	public String saveSchedule(@PathVariable Long productId, 
	@RequestParam String selectedDates,
	RedirectAttributes ra,
	HttpServletRequest request) {
		
		scheduleService.createOrEditSchedule(productId, selectedDates);
		ra.addFlashAttribute("success","Schedule has been successfully added to ProductID: %s".formatted(productId));
	    return "redirect:" + LangPathUtils.buildLangUrl(request, "/admin/schedule");
	}
	
	@GetMapping("/delete-schedule/{productId}")
	public String deleteSchedule(@PathVariable Long productId, 
	RedirectAttributes ra, Model model, 
	HttpServletRequest request) {
		
		scheduleService.deleteSchedule(productId);
		ra.addFlashAttribute("success","Schedule has been successfully deleted to ProductID: %s".formatted(productId));
		return "redirect:" + LangPathUtils.buildLangUrl(request, "/admin/schedule");
	}
}
