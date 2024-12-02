package com.cleaning.cleanify.reservation.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface ReservationFullResponse {
	Long getId();

	String getAddress();

	String getDateTime();

	Integer getNumberOfBathrooms();

	Integer getNumberOfRooms();

	BigDecimal getPrice();

	String getComment();

	BigDecimal getEstimatedTimeHours();

	String getKeyLocation();

	String getStatus();

	String getService();

	Integer getFloor();

	String getApartment();

	List<String> getAdditionalServices();

	String getPhoneNumber();

	String getFirstName();

	Integer getCleaningFrequency();

	String getNextCleaningDate();

	Boolean getIsRegularCleaning();
}
