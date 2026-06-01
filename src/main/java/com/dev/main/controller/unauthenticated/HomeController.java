package com.dev.main.controller.unauthenticated;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dev.main.service.CategoryService;
import com.dev.main.service.ProductService;
import com.dev.main.service.ScheduleService;

@Controller
@RequestMapping("/{lang}/home")
public class HomeController {
	
	private final ProductService productService;
	private final CategoryService categoryService;
	private final ScheduleService scheduleService;

	public HomeController(ProductService productService, CategoryService categoryService,
			ScheduleService scheduleService) {
		super();
		this.productService = productService;
		this.categoryService = categoryService;
		this.scheduleService = scheduleService;
	}

	@GetMapping({"","/"})
	public String homePage(Model model) {
		
		model.addAttribute("products",productService.getAllFeaturedProductsOrderByFeaturedAt());
		model.addAttribute("schedules",scheduleService.getAllSchedulesByDate(LocalDate.now()));
		model.addAttribute("metaTitle","meta.title");
		model.addAttribute("metaDescription","meta.description");
		model.addAttribute("content","public/content/home");
		model.addAttribute("canonical","/home");
		
		return "public/public-layout";
	}
}
