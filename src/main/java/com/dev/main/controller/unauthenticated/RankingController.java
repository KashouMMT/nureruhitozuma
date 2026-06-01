package com.dev.main.controller.unauthenticated;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dev.main.service.ProductService;

@Controller
@RequestMapping("/{lang}/home")
public class RankingController {
	
	private ProductService productService;
	
	public RankingController(ProductService productService) {
		this.productService = productService;
	}
	
	@GetMapping("/ranking")
	public String getRankingPage(Model model) {
		
		model.addAttribute("metaTitle","meta.title");
		model.addAttribute("metaDescription","meta.description");
		model.addAttribute("content","public/content/ranking");
		model.addAttribute("canonical","/ranking");
		model.addAttribute("products",productService.getAllFeaturedProductsOrderByFeaturedAt());
		
		return "public/public-layout";
	}
}
