package com.dev.main.controller.unauthenticated;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dev.main.service.ProductService;

@Controller
@RequestMapping("/{lang}/home")
public class NewcomerController {
	
	private final ProductService productService;
	
	public NewcomerController(ProductService productService) {
		this.productService = productService;
	}
	
	@GetMapping("/newcomer")
	public String getNewcomerPage(Model model) {
		
		model.addAttribute("metaTitle","meta.title");
		model.addAttribute("metaDescription","meta.description");
		model.addAttribute("content","public/content/newcomer");
		model.addAttribute("canonical","/newcomer");
		model.addAttribute("products",productService.getAllNewcomerProductsOrderedByUpdatedAt());
		
		return "public/public-layout";
	}
}
