package com.dev.main.controller.unauthenticated;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dev.main.service.ScheduleService;

@Controller
@RequestMapping("/{lang}/home")
public class ScheduleController {
	
	private final ScheduleService scheduleService;
	
	public ScheduleController(ScheduleService scheduleService) {
		super();
		this.scheduleService = scheduleService;
	}

	@GetMapping("/schedule")
	public String schedulePage(@RequestParam(required = false) Integer day,
		Model model) {
		
		int selectedDay = day == null ? 0 : day;
		
		model.addAttribute("metaTitle","meta.title.schedule");
		model.addAttribute("metaDescription","meta.description.schedule");
		model.addAttribute("content","public/content/schedule");
		model.addAttribute("canonical","/schedule");
		model.addAttribute("today", LocalDate.now());
		model.addAttribute("selectedDay",selectedDay);
		model.addAttribute("schedules",scheduleService.getAllSchedulesByDate(LocalDate.now().plusDays(selectedDay)));
		
		return "public/public-layout";
	}
}
