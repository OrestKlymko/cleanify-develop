package com.cleaning.cleanify.additionalService.service;


import com.cleaning.cleanify.additionalService.dto.AdditionalServiceResponse;
import com.cleaning.cleanify.additionalService.repository.AdditionalServicesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdditionalServiceService {
	private final AdditionalServicesRepository additionalServiceRepository;

	public AdditionalServiceService(AdditionalServicesRepository additionalServiceRepository) {
		this.additionalServiceRepository = additionalServiceRepository;
	}

	public List<AdditionalServiceResponse> getAdditionalServices() {
		return additionalServiceRepository.findAll().stream().map(additionalService ->
				new AdditionalServiceResponse(
						additionalService.getId(),
						additionalService.getKey(),
						additionalService.getValue(),
						additionalService.getPrice())
		).toList();
	}
}
