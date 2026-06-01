package com.dev.main.controller.admin;

import java.util.List;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dev.main.dto.CategoryDto;
import com.dev.main.dto.ProductDto;
import com.dev.main.model.Product;
import com.dev.main.security.MyUserDetails;
import com.dev.main.service.CategoryService;
import com.dev.main.service.ProductImageService;
import com.dev.main.service.ProductService;
import com.dev.main.utils.LangPathUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/{lang}/admin")
public class AdminProductController {
	
    private final ProductService productService;
	private final CategoryService categoryService;
	private final ProductImageService productImageService;

	public AdminProductController(ProductService productService, CategoryService categoryService,
			ProductImageService productImageService) {
		super();
		this.productService = productService;
		this.categoryService = categoryService;
		this.productImageService = productImageService;
	}

	@GetMapping("/product")
	public String productListPage(@AuthenticationPrincipal MyUserDetails userDetails,Model model) {
		
		String username = userDetails.getUsername();
		
		if (username.equals("failed")) {
			return "redirect:/auth/login";
		}
		
		List<Product> products = productService.getAllWithCategoryAndImage();
		model.addAttribute("products",products);
		model.addAttribute("username",username);
		model.addAttribute("content","admin/content/admin-product-tables");
		
		return "admin/admin-layout";
	}
	
	@GetMapping("/add-product")
	public String addProductPage(@AuthenticationPrincipal MyUserDetails userDetails,Locale locale,Model model) {
		
		ProductDto productDto = new ProductDto();
		CategoryDto categoryDto = new CategoryDto();
		model.addAttribute("productDto",productDto);
		model.addAttribute("categoryDto",categoryDto);
		model.addAttribute("categories",categoryService.getAllCategories());
		model.addAttribute("addAction",true);
		model.addAttribute("content","admin/content/admin-product-form");
		model.addAttribute("formAction","/%s%s".formatted(locale.getLanguage(),"/admin/add-product"));

	    return "admin/admin-layout";
	}
	
	@PostMapping("/add-product")
	public String postAddProductPage(
		@Valid @ModelAttribute ProductDto productDto,
		BindingResult result1,
        @Valid @ModelAttribute CategoryDto categoryDto,
        BindingResult result2,
        @RequestParam(required = false) MultipartFile[] images,HttpServletRequest request,
        Model model) {
		
		String lang = LangPathUtils.resolveLangFromPath(request);
		
		if(result1.hasErrors() || result2.hasErrors()) {
			model.addAttribute("productDto",productDto);
			model.addAttribute("categoryDto",categoryDto);
			model.addAttribute("categories",categoryService.getAllCategories());
			model.addAttribute("addAction",true);
			model.addAttribute("formAction","/%s%s".formatted(lang,"/admin/add-product"));
			model.addAttribute("images",images);
			
			model.addAttribute("content","admin/content/admin-product-form");
			
			return "admin/admin-layout";
		}
		
		productService.createProduct(productDto, categoryDto,images);
		
		return "redirect:" + LangPathUtils.buildLangUrl(request, "/admin/product");
	}
	
	@GetMapping("edit-product/{id}")
	public String editProductPage(@PathVariable Long id, @AuthenticationPrincipal MyUserDetails userDetails, Locale locale,
		Model model) {
		
		String username = userDetails.getUsername();
		
		ProductDto productDto = new ProductDto();
		CategoryDto categoryDto = new CategoryDto();
		Product product = productService.getProductWithCategoryById(id);
		productDto.setTitle(product.getTitle());
		productDto.setDescription(product.getDescription());
		productDto.setShortDescription(product.getShortDescription());
		productDto.setAge(product.getAge());
		productDto.setCupSize(product.getCupSize().name());
		productDto.setHeight(product.getHeight());
		productDto.setCategory(product.getCategory());
		productDto.setSortOrder(product.getSortOrder());
		categoryDto.setCategoryName(product.getCategory().getCategoryName());
		
		model.addAttribute("productImages",productImageService.getAllProductImagesByProductId(id));
		model.addAttribute("productDto",productDto);
		model.addAttribute("categoryDto",categoryDto);
		model.addAttribute("categories",categoryService.getAllCategories());
		model.addAttribute("username",username);
		model.addAttribute("formTitle","admin.cast.edit");
		model.addAttribute("formAction","/%s/admin/edit-product/%d".formatted(locale.getLanguage(),id));
		model.addAttribute("content","admin/content/admin-product-form");
		
		return "admin/admin-layout";
	}
	
	@PostMapping("/edit-product/{id}")
	public String postEditProductPage(@Valid @ModelAttribute ProductDto productDto,
			BindingResult result1,
	        @Valid @ModelAttribute CategoryDto categoryDto,
	        BindingResult result2,
	        @PathVariable Long id,
	        @RequestParam(required = false) MultipartFile[] images, HttpServletRequest request,
	        Model model) {
		
		if(result1.hasErrors() || result2.hasErrors()) {			
			model.addAttribute("productDto",productDto);
			model.addAttribute("categoryDto",categoryDto);
			model.addAttribute("categories",categoryService.getAllCategories());
			model.addAttribute("formTitle","admin.cast.edit");
			model.addAttribute("formAction","/%s/admin/edit-product/%d".formatted(LangPathUtils.resolveLangFromPath(request),id));
			model.addAttribute("content","admin/content/admin-product-form");
			
			return "admin/admin-layout";
		}
		
		productService.editProductWithCategory(id, productDto, categoryDto,images);
		
		return "redirect:" + LangPathUtils.buildLangUrl(request, "/admin/product");
	}
	
	@GetMapping("/delete-product/{id}")
	public String deleteProduct(@PathVariable Long id,HttpServletRequest request, RedirectAttributes ra) {
		
		productService.deleteProduct(id);
		ra.addFlashAttribute("success","admin.cast-delete-success-noti");
		
		return "redirect:" + LangPathUtils.buildLangUrl(request, "/admin/product");
	}
	
	@GetMapping("/toggle-featured/{id}")
	public String productToggleFeatured(@PathVariable Long id,HttpServletRequest request,RedirectAttributes ra) {
		
		String lang = LangPathUtils.resolveLangFromPath(request);
		
		productService.toggleFeatured(id);
		ra.addFlashAttribute("success","admin.cast-feature-toggled-noti");
		
		return "redirect:" + LangPathUtils.buildLangUrl(request,lang,"/admin/product");
	}
	
	@GetMapping("/toggle-newcomers/{id}")
	public String productToggleNewcomers(@PathVariable Long id,HttpServletRequest request, RedirectAttributes ra) {
		
		String lang = LangPathUtils.resolveLangFromPath(request);
		
		productService.toggleNewcomers(id);
		ra.addFlashAttribute("success","admin.cast-newcomer-toggled-noti");
		return "redirect:" + LangPathUtils.buildLangUrl(request,lang,"/admin/product");
	}
	
	@GetMapping("/sort-up/{productId}")
	public String productSortUp(@PathVariable Long productId,HttpServletRequest request) {
		
		productService.sortUp(productId);
		
		return "redirect:" + LangPathUtils.buildLangUrl(request,"/admin/product");
	}
	
	@GetMapping("/sort-down/{productId}")
	public String productSortDown(@PathVariable Long productId,HttpServletRequest request) {
		
		productService.sortDown(productId);
		
		return "redirect:" + LangPathUtils.buildLangUrl(request,"/admin/product");
	}
	
	@PostMapping("/swap-to")
	public String productSwapTo(@RequestParam Long from, 
		@RequestParam Long to,HttpServletRequest request) {
		
		productService.moveToPosition(from, to);
		
		return "redirect:" + LangPathUtils.buildLangUrl(request,"/admin/product");
	}
}
