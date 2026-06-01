package com.dev.main.serviceImpl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.dev.main.data.CupSize;
import com.dev.main.dto.CategoryDto;
import com.dev.main.dto.ProductDto;
import com.dev.main.model.Category;
import com.dev.main.model.Product;
import com.dev.main.model.ProductImage;
import com.dev.main.repository.CategoryRepository;
import com.dev.main.repository.ProductImageRepository;
import com.dev.main.repository.ProductRepository;
import com.dev.main.service.ProductImageService;
import com.dev.main.service.ProductService;

import jakarta.persistence.criteria.Predicate;

@Service
public class ProductServiceImpl implements ProductService{
	
	private final ProductRepository productRepo;
	private final CategoryRepository categoryRepo;
	private final ProductImageRepository productImageRepo;
	private final FileStorageServiceImpl fileStorageService;
	private final ProductImageService productImageService;

	public ProductServiceImpl(ProductRepository productRepo, CategoryRepository categoryRepo,
			ProductImageRepository productImageRepo, FileStorageServiceImpl fileStorageService,
			ProductImageService productImageService) {
		super();
		this.productRepo = productRepo;
		this.categoryRepo = categoryRepo;
		this.productImageRepo = productImageRepo;
		this.fileStorageService = fileStorageService;
		this.productImageService = productImageService;
	}

	@Override
	public List<Product> getAllProducts() {
		return productRepo.findAll();
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Product> getAllWithCategoryAndImage() {
		return productRepo.findAllWithCategoryAndImages();
	}
	
	@Override
	public Product getProductById(Long id) {
		return productRepo.findById(id).orElse(null);
	}

	@Override
	public Product getProductWithCategoryById(Long id) {
		return productRepo.findWithCategoryById(id).orElse(null);
	}
	
	@Override
	public List<Product> getAllFeaturedProductsOrderByFeaturedAt() {
		return productRepo.findByFeaturedTrueOrderByFeaturedAtAsc();
	}
	

	@Override
	public Product getProductByProductName(String productName) {
		return productRepo.findByTitle(productName).orElse(null);
	}
	
	@Override
	public List<Product> getAllNewcomerProductsOrderedByUpdatedAt() {
		return productRepo.findByIsNewcomerTrueOrderByUpdatedAtAsc();
	}

	@Override
	public Long getAllProductsCount() {
		return productRepo.count();
	}
	
	@Override
	public void deleteProduct(Long id) {
		List<ProductImage> images = productImageRepo.findByProductIdOrderBySortOrderAsc(id);
		for(ProductImage pi : images) {
			fileStorageService.deleteIfExists(pi.getFilename());
		}
		productRepo.deleteById(id);
	}
	
	@Override
	@Transactional
	public Product createProduct(ProductDto productDto, CategoryDto categoryDto, MultipartFile[] images) {
		Category category = categoryRepo.findByCategoryName(categoryDto.getCategoryName()).orElse(null);
		if(Objects.isNull(category)) {
			category = new Category();
			category.setCategoryName(categoryDto.getCategoryName());
			categoryRepo.save(category);
		}
		Product product = new Product();
		product.setTitle(productDto.getTitle());
		product.setDescription(productDto.getDescription());
		product.setShortDescription(productDto.getShortDescription());
		product.setCategory(category);
		product.setAge(productDto.getAge());
		product.setHeight(productDto.getHeight());
		product.setCupSize(toCupSize(productDto.getCupSize()));
		product = productRepo.save(product);
		for (int i = 0; i < images.length; i++) {
	        MultipartFile file = images[i];
	        if (file != null && !file.isEmpty()) {
	            String stored = fileStorageService.save(file);
	            if (stored != null) {
	                productImageService.createProductImages(stored, product, i);
	            }
	        }
	    }
		Long max= productRepo.findMaxSortOrder();
		product.setSortOrder((max == null ? 0 : max) + 1);
		return product;
	}
	
	@Override
	@Transactional
	public void editProductWithCategory(Long id, ProductDto productDto, CategoryDto categoryDto,MultipartFile[] images) {
		Category category = categoryRepo.findByCategoryName(categoryDto.getCategoryName()).orElse(null);
		if(Objects.isNull(category)) {
			category = new Category();
			category.setCategoryName(categoryDto.getCategoryName());
			categoryRepo.save(category);
		} 
		Product product = getProductById(id);
		product.setDescription(productDto.getDescription());
		product.setShortDescription(productDto.getShortDescription());
		product.setTitle(productDto.getTitle());
		product.setCategory(category);
		product.setAge(productDto.getAge());
		product.setHeight(productDto.getHeight());
		product.setCupSize(toCupSize(productDto.getCupSize()));
		
		boolean hasNoneEmptyImages = images != null && Arrays.stream(images).anyMatch(f -> f != null && !f.isEmpty());
	    
	    if (hasNoneEmptyImages) {
	    	int order = 0;
		    List<ProductImage> old = productImageRepo.findByProductIdOrderBySortOrderAsc(id);
		    for (ProductImage pi : old) fileStorageService.deleteIfExists(pi.getFilename());
		    product.getProductImages().clear(); 
		    productImageRepo.deleteByProductId(id);
	        for (MultipartFile f : images) {
	            if (f == null || f.isEmpty()) continue;
	            String stored = fileStorageService.save(f);
	            ProductImage pi = new ProductImage();
	            pi.setFilename(stored);
	            pi.setAltText(stored);
	            pi.setSortOrder(order);
	            pi.setPrimaryImage(order == 0);
	            pi.setProduct(product);
	            product.getProductImages().add(pi);
	            order++;
	        }
	    }
	    
	    productRepo.save(product);
	}

	@Override
	public void toggleFeatured(Long id) {
		Product product = productRepo.findById(id).orElse(null);
		if (product.isFeatured()) {
			product.setFeatured(false);
			product.setFeaturedAt(null);
		} else {
			product.setFeatured(true);
			product.setFeaturedAt(Instant.now());
		}
		productRepo.save(product);
	}
	
	@Override
	public void toggleNewcomers(Long id) {
		Product product = productRepo.findById(id).orElse(null);
		if(product.isNewcomer()) {
			product.setNewcomer(false);
		} else {
			product.setNewcomer(true);
		}
		productRepo.save(product);
	}
	
	@Override
	@Transactional
	public void sortUp(Long productId) {
		Product current = getProductById(productId);
		
		Long currentOrder = current.getSortOrder();
		if(currentOrder == null) return;
		
		Product previous = productRepo.findFirstBySortOrderLessThanOrderBySortOrderDesc(currentOrder);
		
		if(previous == null) return;
		
		Long prevOrder = previous.getSortOrder();
		previous.setSortOrder(currentOrder);
		current.setSortOrder(prevOrder);
		productRepo.save(previous);
		productRepo.save(current);
	}

	@Override
	@Transactional
	public void sortDown(Long productId) {
		Product current = getProductById(productId);
		
		Long currentOrder = current.getSortOrder();
		if(currentOrder == null) return;
		
		Product next = productRepo.findFirstBySortOrderGreaterThanOrderBySortOrderAsc(currentOrder);
		
		if(next == null) return;
		
		Long nextOrder = next.getSortOrder();
		next.setSortOrder(currentOrder);
		current.setSortOrder(nextOrder);
		
		productRepo.save(next);
		productRepo.save(current);
	}

	@Override
	@Transactional
	public void moveToPosition(Long productId,Long newOrder) {
		Product target = getProductById(productId);
		
		Long oldOrder = target.getSortOrder();
		if(oldOrder == newOrder) return;
		
		if(newOrder < oldOrder) {
			productRepo.shiftRangeUp(newOrder, oldOrder - 1);
		} else {
			productRepo.shiftRangeDown(oldOrder + 1,newOrder);
		}
		target.setSortOrder(newOrder);
	}
	
	@Override
	public List<Product> getAllWithCategoryAndImageFilterBySearch(String name, Long categoryId, String size, String age,
			String height, Pageable pageable) {
		
		int[] ageRange = parseRange(age);
		int[] heightRange = parseRange(height);
		Page<Product> results = productRepo.findAll((root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();

	        if (name != null && !name.isBlank()) {
	            predicates.add(
	                cb.like(cb.lower(root.get("title")), "%" + name.toLowerCase() + "%")
	            );
	        }

	        if (categoryId != null && categoryId != 0) {
	            predicates.add(cb.equal(root.get("category").get("id"), categoryId));
	        }
	        
	        CupSize cupSizeEnum = null;
	        
	        if (size != null && !size.isBlank()) {
	        	
	        	try {
	        		cupSizeEnum = CupSize.valueOf(size.trim().toUpperCase());
	        	} catch (IllegalArgumentException e) {
	                // invalid input (like "big" or "AA"), ignore filter
	                cupSizeEnum = null;
	            }
	        }
	        
	        if (cupSizeEnum != null) {
        	    predicates.add(cb.equal(root.get("cupSize"), cupSizeEnum));
        	}

	        if (ageRange != null) {
	            predicates.add(cb.between(root.get("age"), ageRange[0], ageRange[1]));
	        }

	        if (heightRange != null) {
	            predicates.add(cb.between(root.get("height"), heightRange[0], heightRange[1]));
	        }

	        return cb.and(predicates.toArray(new Predicate[0]));
		},pageable);
		List<Product> products = results.getContent();
		
		return products;
	}
	
	private int[] parseRange(String rangeStr) {
		if(rangeStr == null || rangeStr.isBlank()) {
			return null;
		}
		
		rangeStr = rangeStr.trim().replaceAll("[^0-9\\-]", "");
		String[] parts = rangeStr.split("-");
		try {
			if(parts.length == 2) {
				int min = Integer.parseInt(parts[0].trim());
				int max = Integer.parseInt(parts[1].trim());
				return new int[] {min,max};
			} else if(parts.length == 1) {
				int val = Integer.parseInt(parts[0].trim());
	            return new int[] { val, val };
			}
		} catch (NumberFormatException e) { }
		return null;
	}
	
	private CupSize toCupSize(String value) {
		if (value == null  || value.isBlank()) {
			return CupSize.A_TO_C_CUP;
		}
		try {
			return CupSize.valueOf(value.toUpperCase());
		} catch (IllegalArgumentException ex) {
			throw new IllegalArgumentException("Invalid product status: " + value);
		}
	}
}
