package com.dev.main.controller.unauthenticated;

import java.util.List;

//import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;

import com.dev.main.model.Product;
import com.dev.main.service.CategoryService;
import com.dev.main.service.ProductImageService;
import com.dev.main.service.ProductService;

@Controller
@RequestMapping("/{lang}/home")
public class CastController {
	
	private final ProductService productService;
	private final CategoryService categoryService;
	private final ProductImageService productImageService;

	public CastController(ProductService productService, CategoryService categoryService,
			ProductImageService productImageService) {
		super();
		this.productService = productService;
		this.categoryService = categoryService;
		this.productImageService = productImageService;
	}

	@GetMapping("/cast")
	public String collecionPage(Model model) {
		
		List<Product> products = productService.getAllWithCategoryAndImage();
		for(Product product : products) {
			String shortDescription = shortenDescriptionByChar(product.getDescription(),200);
			product.setDescription(shortDescription);
		}
		
		model.addAttribute("categories",categoryService.getAllCategories());
		model.addAttribute("productsAll",products);
		
		model.addAttribute("metaTitle","meta.title.cast");
		model.addAttribute("metaDescription","meta.description.cast");
		
		model.addAttribute("content","public/content/collection");
		model.addAttribute("canonical","/cast");
		
		return "public/public-layout";
	}
	
//	@GetMapping("/cast/search")
//	public String collectionSearch(@RequestParam(required = false)String name,
//		@RequestParam(required = false) Long categoryId,
//		@RequestParam(required = false) String size,
//		@RequestParam(required = false) String age,
//		@RequestParam(required = false) String height,
//		Model model) {
//		
//		List<Product> products = productService.getAllWithCategoryAndImageFilterBySearch(name, categoryId, size, age, height,Pageable.unpaged());
//		for(Product product : products) {
//			String shortDescription = shortenDescriptionByChar(product.getDescription(),200);
//			product.setDescription(shortDescription);
//		}
//		
//		model.addAttribute("categories",categoryService.getAllCategories());
//		model.addAttribute("products",products);
//		model.addAttribute("content","public/content/collection");
//		model.addAttribute("metaTitle","meta.title.cast");
//		model.addAttribute("metaDescription","meta.description.cast");
//		model.addAttribute("canonical","/cast");
//		
//		return "public/public-layout";
//	}
	
	@GetMapping("/cast/{productId}")
	public String itemPage(@PathVariable Long productId,
		Model model) {
		
		Product product = productService.getProductById(productId);
		
		model.addAttribute("product",product);
		model.addAttribute("productImages",productImageService.getAllProductImagesByProductId(productId));
		model.addAttribute("metaTitle","%s | %s".formatted(product.getTitle(),"【EN Bangkok】"));
		model.addAttribute("metaDescription",product.getShortDescription());
		model.addAttribute("content","public/content/item");
		model.addAttribute("canonical","/cast/" + productId.toString());
		
		return "public/public-layout";
	}
	
//	@GetMapping("/make-reservation")
//	public String makeReservationPage(HttpServletRequest request,@AuthenticationPrincipal MyUserDetails userDetails, 
//		RedirectAttributes ra, Model model) {
//		
//		String lang = LangPathUtils.resolveLangFromPath(request);
//		
//        if (userDetails == null) {
//            ra.addFlashAttribute("warn","Please create an account to make a reservation with a girl.");
//            return "redirect:" + LangPathUtils.buildLangUrl(request, lang, "/auth/sign-up");
//        }
//        
//        String roomId =  "room_" + userDetails.getUsername();
//        if(chatMessageService.isRoomIdExist(roomId)) return "redirect:" + LangPathUtils.buildLangUrl(request, lang, "/user/profile/".concat(roomId));
//        
//        ChatMessageDto chatMessageDto = new ChatMessageDto();
//        chatMessageDto.setRoomId(roomId);
//        
//        if("en".equals(lang)) chatMessageDto.setContent("Thank you for your inquiry. We have received your message regarding this cast and will get back to you shortly.");
//        else chatMessageDto.setContent("お問い合わせいただきあり がとうございます。ご予約に関するメッセージをお受付いたいます。\n担当者より折 り返しご連絡いたします。");
//    
//        chatMessageDto.setSenderId("admin");
//        chatMessageDto.setReceiverId(userDetails.getUsername());
//        lineMessageService.sendInquiry(userDetails.getUsername());
//        chatMessageService.save(chatMessageDto);
//		
//        return "redirect:" + LangPathUtils.buildLangUrl(request, lang, "/user/profile".concat(roomId));
//	}
	
	private String shortenDescriptionByChar(String text, int maxChars) {
	    if (text == null || text.length() <= maxChars) return text;
	    return text.substring(0, maxChars).trim() + " ...";
	}
}
