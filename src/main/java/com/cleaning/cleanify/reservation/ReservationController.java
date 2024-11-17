package com.cleaning.cleanify.reservation;


import com.cleaning.cleanify.reservation.dto.RebookReservationRequest;
import com.cleaning.cleanify.reservation.dto.ReservationCreateRequest;
import com.cleaning.cleanify.reservation.dto.UserReservationResponse;
import com.cleaning.cleanify.reservation.service.ReservationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
	private final ReservationService reservationService;

	public ReservationController(ReservationService reservationService) {
		this.reservationService = reservationService;
	}

	@PostMapping
	public void createReservation(@RequestBody ReservationCreateRequest request) {
		reservationService.createReservation(request);
	}

	@GetMapping
	public List<UserReservationResponse> getReservationsByUser() {
		return reservationService.getReservationsByUser();
	}

	@PostMapping("/rebook")
	public void rebookReservation(@RequestBody RebookReservationRequest request) {
		reservationService.rebookReservation(request);
	}
}
