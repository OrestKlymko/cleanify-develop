package com.cleaning.cleanify.reservation.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RebookReservationRequest(
		Long id,
		Long cleaningType,
		String address,
		LocalDateTime dateTime,
		BigDecimal price,
		String comment,
		String keyLocation,
		BigDecimal estimatedTimeHours
) {
}
