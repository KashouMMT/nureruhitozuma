package com.dev.main.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class BlogDto {
	
	@NotBlank(message = "Title is required")
	@Size(message = "Title must be less than 255",max = 255)
	private String title;
	
	@Size(message = "Content must be less than 250_000",max = 250000)
	private String content;
	
	private MultipartFile image;
	
	private String imageName;
	
	public BlogDto() {
		
	}

	public BlogDto(
			@NotBlank(message = "Title is required") @Size(message = "Title must be less than 255", max = 255) String title,
			@Size(message = "Content must be less than 250_000", max = 250000) String content, MultipartFile image,
			String imageName) {
		super();
		this.title = title;
		this.content = content;
		this.image = image;
		this.imageName = imageName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public MultipartFile getImage() {
		return image;
	}

	public void setImage(MultipartFile image) {
		this.image = image;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
}
