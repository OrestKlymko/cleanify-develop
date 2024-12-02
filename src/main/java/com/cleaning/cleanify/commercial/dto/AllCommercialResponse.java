package com.cleaning.cleanify.commercial.dto;

import java.time.LocalDateTime;

public interface AllCommercialResponse {
	Long getId();

	String getCompanyName();

	String getStatus();

	LocalDateTime getDate();

	String getAddress();

	Integer getFloor();

	Float getSquareFootage();

	String getContactPersonName();

	String getContactPhoneNumber();

	String getAdditionalInstructions();

}
