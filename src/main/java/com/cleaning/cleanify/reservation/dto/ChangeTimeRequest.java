package com.cleaning.cleanify.reservation.dto;

import java.time.LocalDateTime;

public record ChangeTimeRequest(
		Long id,
		LocalDateTime date
) {
}
