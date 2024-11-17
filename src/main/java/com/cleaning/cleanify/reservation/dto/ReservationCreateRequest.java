package com.cleaning.cleanify.reservation.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ReservationCreateRequest(
		String address,
		LocalDateTime date,
		int bedrooms,
		int bathrooms,
		Long cleaningType,
		List<Long> additionalServices,
		BigDecimal price,
		String email,
		String keyLocation,
		String additionalInstructions,
		BigDecimal estimatedTimeHours,
		int floor,
		int apartment
) {
}
