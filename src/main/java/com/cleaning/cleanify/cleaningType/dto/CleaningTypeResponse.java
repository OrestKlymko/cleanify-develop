package com.cleaning.cleanify.cleaningType.dto;

import java.math.BigDecimal;

public record CleaningTypeResponse(
		Long id,
		String key,
		String value,
		BigDecimal price
) {
}
