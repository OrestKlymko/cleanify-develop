package com.cleaning.cleanify.additionalService.dto;

import java.math.BigDecimal;

public record AdditionalServiceResponse(
		Long id,
		String key,
		String value,
		BigDecimal estimatedTimeHours,
		BigDecimal price
) {
}
