package com.dev.main.dto;

import java.time.LocalDate;

public record ScheduleCardDto(
	Long productId,
	String productTitle,
	String productShortDescription,
	LocalDate date,
	String imageFilename
) {}
