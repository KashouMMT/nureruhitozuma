package com.dev.main.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.dev.main.dto.CategoryDto;
import com.dev.main.dto.ProductDto;
import com.dev.main.model.Product;

public interface ProductService {
	List<Product> getAllProducts();
	List<Product> getAllWithCategoryAndImage();
	List<Product> getAllWithCategoryAndImageFilterBySearch(String name,Long categoryId,String size, String age, String height,Pageable pageable);
	List<Product> getAllNewcomerProductsOrderedByUpdatedAt();
	List<Product> getAllFeaturedProductsOrderByFeaturedAt();
	
	Long getAllProductsCount();
	
	Product getProductById(Long id);
	Product getProductByProductName(String productName);
	Product getProductWithCategoryById(Long id);
	Product createProduct(ProductDto productDto, CategoryDto categoryDto, MultipartFile[] images);
	
	void editProductWithCategory(Long id,ProductDto productDto, CategoryDto cateogry,MultipartFile[] images);
	void deleteProduct(Long id);
	void toggleFeatured(Long id);
	void toggleNewcomers(Long id);
	void sortUp(Long productId);
	void sortDown(Long productId);
	void moveToPosition(Long productId,Long newOrder);
	
	//Page<Product> searchProduct(ProductSearchFormDto form);
}
