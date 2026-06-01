package com.dev.main.serviceImpl;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dev.main.dto.BlogDto;
import com.dev.main.model.Blog;
import com.dev.main.model.User;
import com.dev.main.repository.BlogRepository;
import com.dev.main.service.BlogService;

@Service
public class BlogServiceImpl implements BlogService{

	private final BlogRepository blogRepo;
	private final FileStorageServiceImpl storageService;

	public BlogServiceImpl(BlogRepository blogRepo, FileStorageServiceImpl storageService) {
		super();
		this.blogRepo = blogRepo;
		this.storageService = storageService;
	}

	@Override
	public List<Blog> getAllBlogs() {
		return blogRepo.findAll();
	}

	@Override
	public List<Blog> getAllBlogsOrderedByUpdatedAt() {
		return blogRepo.findAllByOrderByUpdatedAtAsc();
	}

	@Override
	public void createBlog(BlogDto blogDto, User user) {
		Blog blog = new Blog();
		blog.setContent(blogDto.getContent());
		blog.setTitle(blogDto.getTitle());
		blog.setUser(user);
		MultipartFile image = blogDto.getImage();
		if(!Objects.isNull(image) && !image.isEmpty()) {
			String filename = storageService.save(image);
			if(filename != null) blog.setImageName(filename);
		}
		blogRepo.save(blog);
	}

	@Override
	public void editBlog(Long id,BlogDto blogDto, User user) {
		Blog blog = getBlogById(id);
		blog.setContent(blogDto.getContent());
		blog.setTitle(blogDto.getTitle());
		blog.setUser(user);
		MultipartFile image = blogDto.getImage();
		if(!Objects.isNull(image) && !image.isEmpty()) {
			storageService.deleteIfExists(blog.getImageName());
			String filename = storageService.save(image);
			if(filename != null) blog.setImageName(filename);
		}
		blogRepo.save(blog);
	}

	@Override
	public void deleteBlog(Long id) {
		blogRepo.deleteById(id);
	}

	@Override
	public Blog getBlogById(Long id) {
		return blogRepo.findById(id).orElse(null);
	}
}
