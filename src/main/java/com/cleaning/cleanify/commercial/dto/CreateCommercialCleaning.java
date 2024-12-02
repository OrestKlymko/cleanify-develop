package com.cleaning.cleanify.commercial.dto;

import java.time.LocalDateTime;

public record CreateCommercialCleaning(
		String address,
		int floor,
		float squareFootage,
		String companyName,
		String contactPersonName,
		String contactPhoneNumber,
		LocalDateTime date,
		String additionalInstructions

) {
}
