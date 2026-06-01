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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dev.main.dto.CategoryDto;
import com.dev.main.model.Category;
import com.dev.main.security.MyUserDetails;
import com.dev.main.service.CategoryService;
import com.dev.main.utils.LangPathUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/{lang}/admin")
public class AdminCategoryController {
	
	private final CategoryService categoryService;

	public AdminCategoryController(CategoryService categoryService) {
		super();
		this.categoryService = categoryService;
	}

	@GetMapping("/category")
	public String categoryListPage(@AuthenticationPrincipal MyUserDetails userDetails,
		Locale locale,Model model) {
		
		String username = userDetails.getUsername();
		
	    model.addAttribute("categories",categoryService.getAllCategoriesWithProducts());
		model.addAttribute("username",username);
		model.addAttribute("content","admin/content/admin-category-tables");
		
		return "admin/admin-layout";
	}
	
	@GetMapping("/add-category")
	public String addCategoryPage(@AuthenticationPrincipal MyUserDetails userDetails,
		Locale locale,Model model) {
		
		String username = userDetails.getUsername();
		
		CategoryDto categoryDto = new CategoryDto();
		
		model.addAttribute("objectDto",categoryDto);
		model.addAttribute("username",username);
		model.addAttribute("formAction","/%s/admin/add-category".formatted(locale.getLanguage()));
		model.addAttribute("content","admin/content/admin-category-form");
		return "admin/admin-layout";
	}
	
	@PostMapping("/add-category")
	public String postAddCategoryPage(@Valid @ModelAttribute("objectDto") CategoryDto categoryDto,
			BindingResult result,HttpServletRequest request,Model model) {
		
		String lang = LangPathUtils.resolveLangFromPath(request);
		
		if (result.hasErrors()) {
			model.addAttribute("objectDto",categoryDto);
			model.addAttribute("formAction","/%s/admin/add-category".formatted(lang));
			model.addAttribute("content","admin/content/admin-category-form");
			
			return "admin/admin-layout";
		}
		categoryService.createCategory(categoryDto);
		
		return "redirect:" + LangPathUtils.buildLangUrl(request, lang,"/admin/category");
	}
	
	@GetMapping("/edit-category/{id}")
	public String editCategoryPage(@PathVariable Long id,Locale locale, Model model) {
		
		CategoryDto categoryDto = new CategoryDto();
		Category category = categoryService.getCategoryById(id);
		categoryDto.setCategoryName(category.getCategoryName());
		
		model.addAttribute("objectDto",categoryDto);
		model.addAttribute("formTitle","admin.category-edit");
		model.addAttribute("formAction","/%s/admin/edit-category/%d".formatted(locale.getLanguage(),id));
		model.addAttribute("content","admin/content/admin-category-form");
		
		return "admin/admin-layout";
	}
	
	@PostMapping("/edit-category/{id}")
	public String postEditCategoryPage(@Valid @ModelAttribute("objectDto") CategoryDto categoryDto,
		BindingResult result,HttpServletRequest request, @PathVariable Long id, Model model) {
		
		String lang = LangPathUtils.resolveLangFromPath(request);
		
		if(result.hasErrors()) {
			model.addAttribute("objectDto",categoryDto);
			model.addAttribute("formTitle","admin.category-edit");
			model.addAttribute("formAction","/%s/admin/edit-category/%d".formatted(lang,id));
			model.addAttribute("content","admin/content/admin-category-form");
			return "admin/admin-layout";
		}
		
		categoryService.editCategory(id, categoryDto);
		
		return "redirect:" + LangPathUtils.buildLangUrl(request, lang,"/admin/category");
	}
	
	@GetMapping("/delete-category/{id}")
	public String deleteCategory(@PathVariable Long id,HttpServletRequest request,RedirectAttributes ra) {
        
		if (categoryService.deleteCategoryIfEmpty(id)) ra.addFlashAttribute("success","admin.category-delete-success-noti");
        else ra.addFlashAttribute("error","admin.category-delete-error-noti");
        
        return "redirect:" + LangPathUtils.buildLangUrl(request, "/admin/category");
	}
}
