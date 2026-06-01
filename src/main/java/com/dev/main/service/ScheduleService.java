package com.dev.main.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.dev.main.dto.ScheduleCardDto;
import com.dev.main.model.Schedule;

public interface ScheduleService {
	public List<Schedule> getAllSchedules();
	public List<ScheduleCardDto> getAllSchedulesByDate(LocalDate date);
	public String getAllSchedulesByProductIdAndReturnAsString(Long productId) ;
	public Map<String, List<ScheduleCardDto>>getGroupedSchedules();
	public void createOrEditSchedule(Long productId,String selectedDates);
	public void deleteSchedule(Long productId);
}
