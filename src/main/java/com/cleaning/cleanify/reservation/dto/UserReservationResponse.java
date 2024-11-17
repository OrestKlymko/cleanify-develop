package com.cleaning.cleanify.reservation.dto;

import java.math.BigDecimal;

public interface UserReservationResponse {
	Long getId();
	String getDateTime();
	String getStatus();
	String getAddress();
	String getService();
	BigDecimal getPrice();
	String getComment();
	String getKeyLocation();
	BigDecimal getEstimatedTimeHours();
}
