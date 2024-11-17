package com.cleaning.cleanify.cleaningType.service;

import com.cleaning.cleanify.cleaningType.dto.CleaningTypeResponse;
import com.cleaning.cleanify.cleaningType.repository.CleaningTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CleaningTypeService {
	private final CleaningTypeRepository cleaningTypeRepository;

	public CleaningTypeService(CleaningTypeRepository cleaningTypeRepository) {
		this.cleaningTypeRepository = cleaningTypeRepository;
	}

	public List<CleaningTypeResponse> getCleaningTypes() {
		return cleaningTypeRepository.findAll().stream().map(cleaningType ->
				new CleaningTypeResponse(
						cleaningType.getId(),
						cleaningType.getKey(),
						cleaningType.getValue(),
						cleaningType.getPrice())
		).toList();
	}
}
