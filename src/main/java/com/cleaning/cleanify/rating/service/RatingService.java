package com.cleaning.cleanify.rating.service;

import com.cleaning.cleanify.auth.service.UserService;
import com.cleaning.cleanify.rating.repository.RatingRepository;
import com.cleaning.cleanify.rating.dto.RateRequest;
import com.cleaning.cleanify.rating.model.Rating;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RatingService {
	private final RatingRepository ratingRepository;
	private final UserService userService;

	public RatingService(RatingRepository ratingRepository, UserService userService) {
		this.ratingRepository = ratingRepository;
		this.userService = userService;
	}

	public void rate(RateRequest request){
		Rating rating = new Rating();
		rating.setComment(request.comment());
		rating.setRating(request.rating());
		rating.setCreatedAt(LocalDateTime.now());
		rating.setUser(userService.getAuthenticatedUser());
		rating.setRating(request.rating());

		ratingRepository.save(rating);
	}
}
