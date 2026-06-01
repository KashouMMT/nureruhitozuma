package com.dev.main.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dev.main.dto.ScheduleCardDto;
import com.dev.main.model.Product;
import com.dev.main.model.Schedule;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long>{
	boolean existsByProductAndDate(Product product, LocalDate date);
	void deleteAllByProductId(Long productId);
	
	List<Schedule> findAllByProductId(Long productId);
	
	@Query("""
	SELECT new com.dev.main.dto.ScheduleCardDto(
		p.id,
		p.title,
		p.shortDescription,
		s.date,
		pi.filename
	)
	FROM Schedule s
	JOIN s.product p
	LEFT JOIN p.productImages pi
	WHERE s.date = :date
	AND pi.sortOrder = 0
	ORDER BY s.date ASC
""")
	List<ScheduleCardDto> findAllByDate(@Param("date") LocalDate date);
	
	@Query("""
	SELECT new com.dev.main.dto.ScheduleCardDto(
		p.id,
		p.title,
		p.shortDescription,
		s.date,
		pi.filename
	)
	FROM Schedule s
	JOIN s.product p
	LEFT JOIN p.productImages pi
	WHERE s.date BETWEEN :startDate AND :endDate
	AND pi.sortOrder = 0
	ORDER BY s.date ASC
	""")
	List<ScheduleCardDto> findScheduleCardBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
