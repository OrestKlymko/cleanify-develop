package com.cleaning.cleanify.rating.dto;

public record RateRequest(
		int rating,
		String comment
) {
}
