package com.cleaning.cleanify.auth.dto;

import java.time.LocalDate;

public record UserUpdateResponse(
		String email,
		String givenName,
		LocalDate birthDate,
		String phone,
		String address
) {
}
