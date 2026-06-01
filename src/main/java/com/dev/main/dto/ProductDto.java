package com.dev.main.dto;

import com.dev.main.model.Category;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ProductDto {
	
	private Long id;
	
	private Category category;
	
	@NotBlank(message = "Title is required")
	@Size(message = "Title must be less than 150",max = 150)
	private String title;
	
	@Size(message = "Description must be less than 250_000",max = 250000)
	private String description;
	
	@Size(message = "Short Description must be less than 255.",max = 255)
	private String shortDescription;
	
	@NotBlank(message = "Cup size is required")
	@Size(message = "Size must not be less than 20",max = 20)
	private String cupSize;
	
	@Min(value = 18, message = "User must be at least 18 years old.") 
	@Max(value = 100, message = "Age cannot exceed 100")
	@NotNull(message = "Age cannot be blank or null.")
	private Integer age = 18;
	
	@Min(value = 1, message = "Height must be at least 1cm") 
	@Max(value = 9999, message = "Height must be less than 9999")
	@NotNull(message = "Height cannot be blank or null.")
	private Integer height = 1;
	
	private Long sortOrder;
	
	public ProductDto() {
		
	}

	public ProductDto(Long id, Category category,
			@NotBlank(message = "Title is required") @Size(message = "Title must be less than 150", max = 150) String title,
			@Size(message = "Description must be less than 250_000", max = 250000) String description,
			@Size(message = "Short Description must be less than 255.", max = 255) String shortDescription,
			@NotBlank(message = "Cup size is required") @Size(message = "Size must not be less than 20", max = 20) String cupSize,
			@Min(value = 18, message = "User must be at least 18 years old.") @Max(value = 100, message = "Age cannot exceed 100") @NotNull(message = "Age cannot be blank or null.") Integer age,
			@Min(value = 1, message = "Height must be at least 1cm") @Max(value = 9999, message = "Height must be less than 9999") @NotNull(message = "Height cannot be blank or null.") Integer height,
			Long sortOrder) {
		super();
		this.id = id;
		this.category = category;
		this.title = title;
		this.description = description;
		this.shortDescription = shortDescription;
		this.cupSize = cupSize;
		this.age = age;
		this.height = height;
		this.sortOrder = sortOrder;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getCupSize() {
		return cupSize;
	}

	public void setCupSize(String cupSize) {
		this.cupSize = cupSize;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Long getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Long sortOrder) {
		this.sortOrder = sortOrder;
	}
}


