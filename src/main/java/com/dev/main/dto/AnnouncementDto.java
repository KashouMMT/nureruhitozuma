package com.dev.main.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AnnouncementDto {
	
	@NotBlank(message = "Title must not be empty")
	@Size(message = "Title must be less than 100",max = 100,min = 0)
	private String title;
	
	@NotBlank(message = "Content must not be empty")
	private String content;
	
	public AnnouncementDto() {
		
	}

	public AnnouncementDto(
			@NotBlank(message = "Title must not be empty") @Size(message = "Title must be less than 100", max = 100, min = 0) String title,
			@NotBlank(message = "Content must not be empty") String content) {
		super();
		this.title = title;
		this.content = content;
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
}
