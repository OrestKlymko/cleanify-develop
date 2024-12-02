package com.cleaning.cleanify.commercial.controller;


import com.cleaning.cleanify.commercial.dto.AllCommercialResponse;
import com.cleaning.cleanify.commercial.dto.ConfirmationRequest;
import com.cleaning.cleanify.commercial.dto.CreateCommercialCleaning;
import com.cleaning.cleanify.commercial.service.CommercialCleaningService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/commercial")
public class CommercialController {
	private final CommercialCleaningService commercialService;

	public CommercialController(CommercialCleaningService commercialService) {
		this.commercialService = commercialService;
	}

	@PostMapping
	public void createCommercialCleaning(@RequestBody CreateCommercialCleaning createCommercialCleaning) {
		commercialService.createCommercialCleaning(createCommercialCleaning);
	}

	@PostMapping("/confirm")
	public void confirmCommercialCleaning(@RequestBody ConfirmationRequest request) {
		commercialService.confirmCommercialCleaning(request);
	}

	@PutMapping("/{id}")
	public void updateCommercialCleaning(@PathVariable Long id, @RequestBody LocalDateTime newTime) {
		commercialService.changeDateOfCommercialCleaning(id, newTime);
	}

	@PostMapping("/cancel/{id}")
	public void cancelCommercialCleaning(@PathVariable Long id) {
		commercialService.cancelCommercialCleaning(id);
	}

	@GetMapping
	public List<AllCommercialResponse> getCommercialCleaning() {
		return commercialService.getCommercialCleaning();
	}

	@GetMapping("/private")
	public List<AllCommercialResponse> getCommercialCleaningByUser() {
		return commercialService.getCommercialCleaningByUser();
	}


}
