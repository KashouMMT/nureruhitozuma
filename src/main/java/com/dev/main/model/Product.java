package com.dev.main.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.dev.main.data.CupSize;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Basic;
import jakarta.persistence.Cacheable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "products")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	@Column(nullable = false, length = 150)
	private String title;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "cup_size",nullable = false, length = 20)
	private CupSize cupSize = CupSize.A_TO_C_CUP;
	
	@Positive @Min(18) @Max(100) @Digits(integer = 3, fraction = 0)
	@Column(nullable = false)
	private int age = 18;
	
	@Positive @Min(1) @Max(9999) @Digits(integer = 4, fraction = 0)
	@Column(nullable = false)
	private int height = 1;
	
	@NotNull
	@Column(name = "short_description",nullable = false, length = 255)
	private String shortDescription;
	
	@NotNull
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(nullable = false, columnDefinition = "TEXT")
	private String description;
	
	@Column(nullable = false)
	private boolean featured = false;
	
	@Column
	private Instant featuredAt;
	
	@Column(name="is_newcomer", nullable = false)
	private boolean isNewcomer = false;
	
	@Column(name = "sort_order")
	private Long sortOrder;
	
	@NotNull
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;
	
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(
		name = "category_id",
		nullable = false, 
		foreignKey = @ForeignKey(name = "fk_product_category"))
	@JsonBackReference
	private Category category;
	
	@OneToMany(
		mappedBy = "product",
	    cascade = CascadeType.ALL,
	    fetch = FetchType.EAGER,
	    orphanRemoval = true)
	@OrderBy("sortOrder ASC, id ASC")
	private List<ProductImage> productImages = new ArrayList<>();
	
	@OneToMany(
		mappedBy = "product",
		cascade = CascadeType.ALL,
		fetch = FetchType.LAZY,
		orphanRemoval = true)
	private List<Schedule> schedules = new ArrayList<>();
	
	public String getMainProductImage() {
		return productImages.getFirst().getFilename();
	}
	
	@PrePersist
	public void prePersit() {
		createdAt = LocalDateTime.now();
	}
	
	@PreUpdate
	public void preUpdate() {
		updatedAt = LocalDateTime.now();
	}

	public Product() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public CupSize getCupSize() {
		return cupSize;
	}

	public void setCupSize(CupSize cupSize) {
		this.cupSize = cupSize;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isFeatured() {
		return featured;
	}

	public void setFeatured(boolean featured) {
		this.featured = featured;
	}

	public Instant getFeaturedAt() {
		return featuredAt;
	}

	public void setFeaturedAt(Instant featuredAt) {
		this.featuredAt = featuredAt;
	}

	public boolean isNewcomer() {
		return isNewcomer;
	}

	public void setNewcomer(boolean isNewcomer) {
		this.isNewcomer = isNewcomer;
	}

	public Long getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Long sortOrder) {
		this.sortOrder = sortOrder;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public List<ProductImage> getProductImages() {
		return productImages;
	}

	public void setProductImages(List<ProductImage> productImages) {
		this.productImages = productImages;
	}

	public List<Schedule> getSchedules() {
		return schedules;
	}

	public void setSchedules(List<Schedule> schedules) {
		this.schedules = schedules;
	}
}
