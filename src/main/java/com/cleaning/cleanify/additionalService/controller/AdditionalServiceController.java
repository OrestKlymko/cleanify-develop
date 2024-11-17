package com.cleaning.cleanify.additionalService.controller;


import com.cleaning.cleanify.additionalService.dto.AdditionalServiceResponse;
import com.cleaning.cleanify.additionalService.service.AdditionalServiceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/additional-service")
public class AdditionalServiceController {
	private final AdditionalServiceService additionalServiceService;

	public AdditionalServiceController(AdditionalServiceService additionalServiceService) {
		this.additionalServiceService = additionalServiceService;
	}

	@GetMapping
	public List<AdditionalServiceResponse> getAdditionalServices() {
		return additionalServiceService.getAdditionalServices();
	}
}
