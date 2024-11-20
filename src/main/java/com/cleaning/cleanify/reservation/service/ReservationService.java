package com.cleaning.cleanify.reservation.service;


import com.cleaning.cleanify.additionalService.model.AdditionalServices;
import com.cleaning.cleanify.additionalService.repository.AdditionalServicesRepository;
import com.cleaning.cleanify.auth.service.UserService;
import com.cleaning.cleanify.auth.model.User;
import com.cleaning.cleanify.cleaningType.repository.CleaningTypeRepository;
import com.cleaning.cleanify.reservation.dto.RebookReservationRequest;
import com.cleaning.cleanify.reservation.dto.ReservationCreateRequest;
import com.cleaning.cleanify.reservation.dto.ReservationFullResponse;
import com.cleaning.cleanify.reservation.dto.UserReservationResponse;
import com.cleaning.cleanify.reservation.model.Reservation;
import com.cleaning.cleanify.reservation.model.State;
import com.cleaning.cleanify.reservation.repository.ReservationRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationService {
	private final ReservationRepository reservationRepository;
	private final UserService userService;
	private final CleaningTypeRepository cleaningTypeRepository;
	private final AdditionalServicesRepository additionalServicesRepository;

	public ReservationService(ReservationRepository reservationRepository, UserService userService, CleaningTypeRepository cleaningTypeRepository, AdditionalServicesRepository additionalServicesRepository) {
		this.reservationRepository = reservationRepository;
		this.userService = userService;
		this.cleaningTypeRepository = cleaningTypeRepository;
		this.additionalServicesRepository = additionalServicesRepository;
	}


	@Transactional
	public void createReservation(ReservationCreateRequest reservationCreateRequest) {
		User authenticatedUser = userService.getAuthenticatedUser();
		authenticatedUser.setAddress(reservationCreateRequest.address());
		Reservation reservation = new Reservation();
		reservation.setAddress(reservationCreateRequest.address());
		reservation.setDate(reservationCreateRequest.date());
		reservation.setBedrooms(reservationCreateRequest.bedrooms());
		reservation.setKeyLocation(reservationCreateRequest.keyLocation());
		reservation.setBathrooms(reservationCreateRequest.bathrooms());
		reservation.setAdditionalInstructions(reservationCreateRequest.additionalInstructions());
		reservation.setPrice(reservationCreateRequest.price());
		reservation.setEstimatedTimeHours(reservationCreateRequest.estimatedTimeHours());
		reservation.setCleaningType(cleaningTypeRepository.findById(reservationCreateRequest.cleaningType()).orElseThrow());
		reservation.setFloor(reservationCreateRequest.floor());
		reservation.setApartment(reservationCreateRequest.apartment());
		reservation.setUser(authenticatedUser);

		List<Long> additionalServicesIds = reservationCreateRequest.additionalServices();
		List<AdditionalServices> additionalServices = additionalServicesRepository.findAllById(additionalServicesIds);

		reservation.setAdditionalServices(additionalServices);
		reservation.setState(State.NEW);

		reservationRepository.save(reservation);
	}

	public List<UserReservationResponse> getReservationsByUser() {
		User user = userService.getAuthenticatedUser();
		return reservationRepository.getReservationsByUser(user.getId());
	}

	public List<ReservationFullResponse> getAllReservationsForAdmin() {
		return reservationRepository.getAllReservationsForAdmin();
	}

	public void rebookReservation(RebookReservationRequest request) {
		Reservation reservation = reservationRepository.findById(request.id()).orElseThrow();
		Reservation newReservation = new Reservation();

		newReservation.setCleaningType(cleaningTypeRepository.findById(request.cleaningType()).orElseThrow());
		newReservation.setAddress(request.address());
		newReservation.setDate(request.dateTime());
		newReservation.setPrice(request.price());
		newReservation.setUser(reservation.getUser());
		newReservation.setState(State.NEW);
		newReservation.setBedrooms(reservation.getBedrooms());
		newReservation.setBathrooms(reservation.getBathrooms());
		newReservation.setKeyLocation(request.keyLocation());
		newReservation.setAdditionalInstructions(request.comment());
		newReservation.setEstimatedTimeHours(request.estimatedTimeHours());
		newReservation.setFloor(reservation.getFloor());
		newReservation.setApartment(reservation.getApartment());

		List<AdditionalServices> newAdditionalServices = new ArrayList<>(reservation.getAdditionalServices());
		newReservation.setAdditionalServices(newAdditionalServices);
		reservationRepository.save(newReservation);
	}


	public void cancelReservation(Long id) {
		Reservation reservation = reservationRepository.findById(id).orElseThrow();
		reservation.setState(State.CANCELLED);
		reservationRepository.save(reservation);
	}

	public void completeReservation(Long id) {
		Reservation reservation = reservationRepository.findById(id).orElseThrow();
		reservation.setState(State.COMPLETED);
		reservationRepository.save(reservation);
	}




}
