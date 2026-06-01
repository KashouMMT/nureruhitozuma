package com.dev.main.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dev.main.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product>{
	
	@Query("select coalesce(max(p.sortOrder),0) from Product p")
	Long findMaxSortOrder();
   
    Long countByCategoryId(Long id);
    
	@EntityGraph(attributePaths = {"category", "productImages"})
	@Query("select p from Product p order by p.sortOrder asc")
	List<Product> findAllWithCategoryAndImages();
    
    List<Product> findByIsNewcomerTrueOrderByUpdatedAtAsc();
    
    List<Product> findByFeaturedTrueOrderByFeaturedAtAsc();
    
    List<Product> findAllByOrderByCreatedAtAsc();
    
    @EntityGraph(attributePaths = {"category"})
    Optional<Product> findWithCategoryById(Long id);
    
    Optional<Product> findByTitle(String title);
    
    Product findFirstBySortOrderLessThanOrderBySortOrderDesc(Long sortOrder);
    
    Product findFirstBySortOrderGreaterThanOrderBySortOrderAsc(Long sortOrder);
    
    @Query("""
    	select p 
    	from Product p 
    	where p.category.id = :categoryId
    	order by
    		case when p.featured = true then 0 else 1 end,
    		p.featuredAt desc,
    		p.id desc
    """)
    Page<Product> findTopFeaturedByCategoryId(@Param("categoryId")Long categoryId,Pageable pageable);
    
    @Modifying
    @Query("""
        UPDATE Product p 
        SET p.sortOrder = p.sortOrder + 1 
        WHERE p.sortOrder >= :start AND p.sortOrder <= :end
    """)
    void shiftRangeUp(@Param("start") Long start, @Param("end") Long end);

    @Modifying
    @Query("""
        UPDATE Product p 
        SET p.sortOrder = p.sortOrder - 1 
        WHERE p.sortOrder >= :start AND p.sortOrder <= :end
    """)
    void shiftRangeDown(@Param("start") Long start, @Param("end") Long end);
}
