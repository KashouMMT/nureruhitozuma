package com.dev.main.serviceImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.dev.main.dto.ScheduleCardDto;
import com.dev.main.model.Product;
import com.dev.main.model.Schedule;
import com.dev.main.repository.ProductRepository;
import com.dev.main.repository.ScheduleRepository;
import com.dev.main.service.ScheduleService;

import jakarta.transaction.Transactional;

@Service
public class ScheduleServiceImpl implements ScheduleService{
	
	private static final Logger logger = LoggerFactory.getLogger(ScheduleServiceImpl.class);
	
	private final ScheduleRepository scheduleRepo;
	private final ProductRepository productRepo;
	
	public ScheduleServiceImpl(ScheduleRepository scheduleRepo, ProductRepository productRepo) {
		super();
		this.scheduleRepo = scheduleRepo;
		this.productRepo = productRepo;
	}
	
	@Override
	@Transactional
	public void createOrEditSchedule(Long productId, String selectedDates) {
		if(selectedDates == null || selectedDates.isBlank()) {
			logger.warn("Schedule Date are null. Cannot create schedule");
			return;
		}
		
		Product product = productRepo.findById(productId).orElse(null);
		
		if (product == null) {
			logger.warn("Product is null. Cannot create schedule.");
			return;
		}
		
		scheduleRepo.deleteAllByProductId(productId);
		scheduleRepo.flush();
		
		List<LocalDate> dates = Arrays.stream(selectedDates.split(","))
				.map(String::trim)
				.map(LocalDate::parse)
				.distinct()
				.toList();
		
		List<Schedule> schedules = new ArrayList<>();
		
		for(LocalDate date : dates) {
			Schedule schedule = new Schedule();
			schedule.setProduct(product);
			schedule.setDate(date);
			schedules.add(schedule);
		}
		scheduleRepo.saveAll(schedules);
	}
	
	@Override
	public Map<String, List<ScheduleCardDto>>getGroupedSchedules() {

	    List<ScheduleCardDto> schedules = scheduleRepo.findScheduleCardBetweenDates(
            LocalDate.now(),
            LocalDate.now().plusDays(7)
	    );

	    LocalDate today = LocalDate.now();

	    Map<String, List<ScheduleCardDto>> grouped = new LinkedHashMap<>();

	    grouped.put("day_1", schedules.stream().filter(s ->
            s.date().equals(today)).toList());

	    grouped.put("day_2", schedules.stream().filter(s ->
            s.date().equals(today.plusDays(1))).toList());

	    grouped.put("day_3", schedules.stream().filter(s ->
            s.date().equals(today.plusDays(2))).toList());
	    
	    grouped.put("day_4", schedules.stream().filter(s ->
        	s.date().equals(today.plusDays(3))).toList());
	    
	    grouped.put("day_5", schedules.stream().filter(s ->
	    	s.date().equals(today.plusDays(4))).toList());
	    
	    grouped.put("day_6", schedules.stream().filter(s ->
        	s.date().equals(today.plusDays(5))).toList());
	    
	    grouped.put("day_7", schedules.stream().filter(s ->
        	s.date().equals(today.plusDays(6))).toList());

	    return grouped;
	}
	
	@Override
	public List<ScheduleCardDto> getAllSchedulesByDate(LocalDate date) {
		
		return scheduleRepo.findAllByDate(date);
	}
	
	@Override
	public List<Schedule> getAllSchedules() {
		return scheduleRepo.findAll();
	}
	
	@Override
	@Transactional
	public void deleteSchedule(Long productId) {
		scheduleRepo.deleteAllByProductId(productId);
	}
	
	@Override
	public String getAllSchedulesByProductIdAndReturnAsString(Long productId) {
		Product product = productRepo.findById(productId).orElse(null);
		if (product == null) {
			logger.warn("Product is null. Cannot create schedule.");
			return "";
		}
		List<Schedule> schedules = scheduleRepo.findAllByProductId(productId);
		
		return schedules.stream()
			.map(Schedule::getDate)
			.sorted()
			.map(LocalDate::toString)
			.collect(Collectors.joining(","));
	}
}
