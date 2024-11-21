package com.cleaning.cleanify.reservation;


import com.cleaning.cleanify.reservation.dto.*;
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

	@GetMapping("/admin")
	public List<ReservationFullResponse> getAllReservationsForAdmin() {
		return reservationService.getAllReservationsForAdmin();
	}

	@PostMapping("/rebook")
	public void rebookReservation(@RequestBody RebookReservationRequest request) {
		reservationService.rebookReservation(request);
	}

	@PutMapping
	public void changeTimeOfReservation(@RequestBody ChangeTimeRequest request) {
		reservationService.changeTimeOfReservation(request);
	}

	@DeleteMapping("/{id}")
	public void cancelReservation(@PathVariable Long id) {
		reservationService.cancelReservation(id);
	}

	@PostMapping("/confirm/{id}")
	public void confirmReservation(@PathVariable Long id) {
		reservationService.confirmReservation(id);
	}
}
