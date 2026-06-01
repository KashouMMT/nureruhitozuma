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

import com.dev.main.dto.BlogDto;
import com.dev.main.model.Blog;
import com.dev.main.security.MyUserDetails;
import com.dev.main.service.BlogService;
import com.dev.main.service.UserService;
import com.dev.main.utils.LangPathUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/{lang}/admin")
public class AdminBlogController {
	
	private final BlogService blogService;
	private final UserService userService;

	public AdminBlogController(BlogService blogService, UserService userService) {
		super();
		this.blogService = blogService;
		this.userService = userService;
	}

	@GetMapping("/blog")
	public String getBlogPage(Model model) {
		
		model.addAttribute("content","admin/content/admin-blog");
		model.addAttribute("blogs",blogService.getAllBlogsOrderedByUpdatedAt());
		
		return "admin/admin-layout";
	}
	
	@GetMapping("/add-blog")
	public String getAddBlogPage(Locale locale,Model model) {
		
		BlogDto blogDto = new BlogDto();
		
		model.addAttribute("objectDto",blogDto);
		model.addAttribute("formAction","/%s/admin/add-blog".formatted(locale.getLanguage()));
		model.addAttribute("content","admin/content/admin-blog-form");
		
		return "admin/admin-layout";
	}
	
	@PostMapping("/add-blog")
	public String postAddBlogPage(@Valid @ModelAttribute("objectDto") BlogDto blogDto,
	BindingResult result, @AuthenticationPrincipal MyUserDetails userDetails,
	HttpServletRequest request,Model model) {
		
		String lang = LangPathUtils.resolveLangFromPath(request);
		
		if(result.hasErrors()) {
			model.addAttribute("objectDto",blogDto);
			model.addAttribute("formAction","/%s/admin/add-blog".formatted(lang));
			model.addAttribute("content","admin/content/admin-blog-form");
			
			return "admin/admin-layout";
		}
		blogService.createBlog(blogDto, userService.getUserById(userDetails.getId()));
		
		return "redirect:" + LangPathUtils.buildLangUrl(request, "/admin/blog");
	}
	
	@GetMapping("/edit-blog/{id}")
	public String getEditBlogPage(@PathVariable Long id,Locale locale,Model model) {
		
		Blog blog = blogService.getBlogById(id);
		BlogDto blogDto = new BlogDto();
		blogDto.setTitle(blog.getTitle());
		blogDto.setContent(blog.getContent());
		blogDto.setImageName(blog.getImageName());
		
		model.addAttribute("objectDto",blogDto);
		model.addAttribute("blog",blogService.getBlogById(id));
		model.addAttribute("formAction","/%s/admin/edit-blog/%d".formatted(locale.getLanguage(),id));
		model.addAttribute("formTitle","admin.blog.edit");
		model.addAttribute("content","admin/content/admin-blog-form");
		
		return "admin/admin-layout";
	}
	
	@PostMapping("/edit-blog/{id}")
	public String postEditBlogPage(@Valid @ModelAttribute("objectDto")BlogDto blogDto,BindingResult result, 
		@PathVariable Long id,@AuthenticationPrincipal MyUserDetails userDetails, HttpServletRequest request,Model model) {
		
		String lang = LangPathUtils.resolveLangFromPath(request);
		
		if(result.hasErrors()) {
			model.addAttribute("blog",blogService.getBlogById(id));
			model.addAttribute("objectDto",blogDto);
			model.addAttribute("formAction","/%s/admin/edit-blog/%d".formatted(lang,id));
			model.addAttribute("formTitle","admin.blog.edit");
			model.addAttribute("content","admin/content/admin-blog-form");
			return "admin/admin-layout";
		}
		
		blogService.editBlog(id, blogDto, userService.getUserById(userDetails.getId()));
		
		return "redirect:" + LangPathUtils.buildLangUrl(request, lang,"/admin/blog") ;
	}
	
	@GetMapping("/delete-blog/{id}")
	public String deleteBlog(@PathVariable Long id,HttpServletRequest request,Model model,RedirectAttributes ra) {
		
		blogService.deleteBlog(id);
		
		return "redirect:" + LangPathUtils.buildLangUrl(request, "/admin/blog");
	}
}
