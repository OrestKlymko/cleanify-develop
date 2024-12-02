package com.cleaning.cleanify.commercial.dto;

import java.math.BigDecimal;

public record ConfirmationRequest(
		Long id,
		BigDecimal price,
		String additionalInstructions
) {
}
