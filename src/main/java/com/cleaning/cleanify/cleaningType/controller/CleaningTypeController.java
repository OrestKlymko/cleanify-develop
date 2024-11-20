package com.cleaning.cleanify.cleaningType.controller;

import com.cleaning.cleanify.cleaningType.dto.CleaningTypeResponse;
import com.cleaning.cleanify.cleaningType.service.CleaningTypeService;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cleaning-type")
public class CleaningTypeController {
	private final CleaningTypeService cleaningTypeService;

	public CleaningTypeController(CleaningTypeService cleaningTypeService) {
		this.cleaningTypeService = cleaningTypeService;
	}

	@GetMapping
	public List<CleaningTypeResponse> getCleaningTypes() {
		System.out.println("CleaningTypeController.getCleaningTypes");
		return cleaningTypeService.getCleaningTypes();
	}

	@ExceptionHandler(Exception.class)
	public String handleException(Exception e) {
		return e.getMessage();
	}
}
