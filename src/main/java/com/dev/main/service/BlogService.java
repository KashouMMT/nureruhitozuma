package com.dev.main.service;

import java.util.List;

import com.dev.main.dto.BlogDto;
import com.dev.main.model.Blog;
import com.dev.main.model.User;

public interface BlogService {
	List<Blog> getAllBlogs();
	List<Blog> getAllBlogsOrderedByUpdatedAt();
	
	Blog getBlogById(Long id);
	
	void createBlog(BlogDto blogDto, User user);
	void editBlog(Long id,BlogDto blogDto, User user);
	void deleteBlog(Long id);
}
