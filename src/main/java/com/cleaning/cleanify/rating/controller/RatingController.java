package com.cleaning.cleanify.rating.controller;


import com.cleaning.cleanify.rating.dto.RateRequest;
import com.cleaning.cleanify.rating.service.RatingService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {
	private final RatingService ratingService;

	public RatingController(RatingService ratingService) {
		this.ratingService = ratingService;
	}

	@PostMapping
	public void rate(@RequestBody RateRequest request) {
		ratingService.rate(request);
	}

}
