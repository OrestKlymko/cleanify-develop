package com.cleaning.cleanify.commercial.dto;

import com.cleaning.cleanify.reservation.model.State;

public record CommercialResponse(
		Long id,
		String companyName,
		State status,
		String date,
		String address,
		Integer floor,
		float squareFootage,
		String contactPersonName,
		String contactPhoneNumber,
		String additionalInstructions
) {
}
