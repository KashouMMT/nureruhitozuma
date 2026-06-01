package com.dev.main.controller.unauthenticated;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/{lang}/home")
public class SystemController {
	
	@GetMapping("/system")
	public String getSystemPage(Model model) {
		
		model.addAttribute("metaTitle","meta.title.system");
		model.addAttribute("metaDescription","meta.description.system");
		model.addAttribute("content","public/content/system");
		model.addAttribute("canonical","/system");
		
		return "public/public-layout";
	}
}
