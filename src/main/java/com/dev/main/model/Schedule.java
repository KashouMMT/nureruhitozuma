package com.dev.main.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.esotericsoftware.kryo.serializers.FieldSerializer.NotNull;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.FutureOrPresent;

@Entity
@Table(
	name = "schedules",
	uniqueConstraints = @UniqueConstraint(
			name = "uk_schedule_product_date",
			columnNames = {"product_id","date"}
	)
)
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Schedule {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@FutureOrPresent
	@Column(name = "date", nullable = false)
	private LocalDate date;
	
	@NotNull
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;
	
	@NotNull
	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;
	
	@ManyToOne
	@JoinColumn(
		name = "product_id",
		nullable = false,
		foreignKey = @ForeignKey(name = "fk_schedule_product")
	)
	@JsonManagedReference
	private Product product;
	
	@PrePersist
	public void prePersist() {
		createdAt = LocalDateTime.now();
		updatedAt = LocalDateTime.now();
	}
	
	@PreUpdate
	public void preUpdate() {
		updatedAt = LocalDateTime.now();
	}
	
	public Schedule() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
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

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
}
