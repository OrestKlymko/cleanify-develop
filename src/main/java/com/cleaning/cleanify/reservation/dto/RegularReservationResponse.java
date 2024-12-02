package com.cleaning.cleanify.reservation.dto;

import java.math.BigDecimal;

public interface RegularReservationResponse {
	Long getId();
	String getDateTime();
	BigDecimal getPrice();
	String getCustomerId();
	String getFirstName();
	String getEmail();
	Integer getCleaningFrequency();
}
