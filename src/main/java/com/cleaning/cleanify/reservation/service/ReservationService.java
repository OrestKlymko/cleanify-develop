package com.cleaning.cleanify.reservation.service;


import com.cleaning.cleanify.additionalService.model.AdditionalServices;
import com.cleaning.cleanify.additionalService.repository.AdditionalServicesRepository;
import com.cleaning.cleanify.auth.service.UserService;
import com.cleaning.cleanify.auth.model.User;
import com.cleaning.cleanify.cleaningType.repository.CleaningTypeRepository;
import com.cleaning.cleanify.mail.MailService;
import com.cleaning.cleanify.payment.service.PaymentService;
import com.cleaning.cleanify.reservation.dto.*;
import com.cleaning.cleanify.reservation.model.Reservation;
import com.cleaning.cleanify.reservation.model.State;
import com.cleaning.cleanify.reservation.repository.ReservationRepository;
import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationService {
	private final ReservationRepository reservationRepository;
	private final UserService userService;
	private final CleaningTypeRepository cleaningTypeRepository;
	private final AdditionalServicesRepository additionalServicesRepository;
	private final PaymentService paymentService;
	private final MailService mailService;
	private static final Logger log = org.slf4j.LoggerFactory.getLogger(ReservationService.class);

	public ReservationService(ReservationRepository reservationRepository, UserService userService, CleaningTypeRepository cleaningTypeRepository, AdditionalServicesRepository additionalServicesRepository, PaymentService paymentService, MailService mailService) {
		this.reservationRepository = reservationRepository;
		this.userService = userService;
		this.cleaningTypeRepository = cleaningTypeRepository;
		this.additionalServicesRepository = additionalServicesRepository;
		this.paymentService = paymentService;
		this.mailService = mailService;
	}


	@Transactional
	public void createReservation(ReservationCreateRequest reservationCreateRequest) {
		User authenticatedUser = setUserDetail(reservationCreateRequest);
		Reservation reservation = createReservation(reservationCreateRequest, authenticatedUser);
		mailService.sendMessageNewAppointment("cleanifybee@gmail.com", authenticatedUser, reservation);
		reservationRepository.save(reservation);
	}

	@NotNull
	private Reservation createReservation(ReservationCreateRequest reservationCreateRequest, User authenticatedUser) {
		Reservation reservation = new Reservation();
		List<Long> additionalServicesIds = reservationCreateRequest.additionalServices();
		List<AdditionalServices> additionalServices = additionalServicesRepository.findAllById(additionalServicesIds);
		if (reservationCreateRequest.isRegularCleaning()) {
			reservation.setCleaningFrequency(reservationCreateRequest.cleaningFrequency());
			reservation.setNextCleaningDate(reservationCreateRequest.date().plusDays(reservationCreateRequest.cleaningFrequency()));
		}
		reservation.setRegularCleaning(reservationCreateRequest.isRegularCleaning());
		reservation.setPaymentMethod(reservationCreateRequest.paymentMethod());
		reservation.setAdditionalServices(additionalServices);
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
		reservation.setState(State.NEW);
		return reservation;
	}

	@NotNull
	private User setUserDetail(ReservationCreateRequest reservationCreateRequest) {
		User authenticatedUser = userService.getAuthenticatedUser();
		authenticatedUser.setAddress(reservationCreateRequest.address());
		authenticatedUser.setFirstName(reservationCreateRequest.fullName());
		authenticatedUser.setPhoneNumber(reservationCreateRequest.phoneNumber());
		return authenticatedUser;
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
		mailService.sendMessageNewAppointment("cleanifybee@gmail.com", reservation.getUser(), newReservation);
		reservationRepository.save(newReservation);
	}


	public void cancelReservation(Long id) {
		Reservation reservation = reservationRepository.findById(id).orElseThrow();
		reservation.setState(State.CANCELLED);
		mailService.sendCancellationReservationMail(reservation.getUser().getEmail(), reservation.getUser(), reservation);
		reservationRepository.save(reservation);
	}

	public void changeTimeOfReservation(ChangeTimeRequest request) {
		Reservation reservation = reservationRepository.findById(request.id()).orElseThrow();
		reservation.setDate(request.date());
		reservationRepository.save(reservation);
		mailService.sendMessageChangeTimeOfReservation(reservation.getUser().getEmail(), reservation.getUser(), reservation);
	}

	@Transactional
	public void confirmReservation(Long id) {
		Reservation reservation = reservationRepository.findById(id).orElseThrow();
		try {
			paymentService.chargeCustomer(reservation.getPrice(), reservation.getUser().getCustomerId());
			if (reservation.getRegularCleaning()) {
				reservation.setState(State.REGULAR);
				reservation.setNextCleaningDate(reservation.getDate().plusDays(reservation.getCleaningFrequency()));
			} else {
				reservation.setState(State.ACCEPTED);
			}
			reservationRepository.save(reservation);
			mailService.sendConfirmationReservationMail(reservation.getUser().getEmail(), reservation.getUser(), reservation);
		} catch (Exception e) {
			log.error("Error confirming reservation", e);
		}
	}

	@Transactional
	public void cancelRegularReservation(Long id) {
		Reservation reservation = reservationRepository.findById(id).orElseThrow();
		reservation.setState(State.CANCELLED);
		reservationRepository.save(reservation);
		mailService.sendCancellationReservationMail(reservation.getUser().getEmail(), reservation.getUser(), reservation);
	}


	@Scheduled(cron = "0 7 8 * * *")
	@Transactional
	public void chargePaymentOnRegularReservations() {
		log.info("Charging payment on regular reservations");
		List<RegularReservationResponse> reservations = reservationRepository.getRegularReservationsOfCurrentDate();
		for (RegularReservationResponse reservation : reservations) {
			try {
				paymentService.chargeCustomer(reservation.getPrice(), reservation.getCustomerId());
				reservationRepository.updateDateOfRegularReservation(reservation.getId(), reservation.getCleaningFrequency());
				mailService.sendConfirmationOfTodayRegularCleaning(reservation.getEmail(), reservation.getFirstName());
				log.info("Payment charged for regular reservation with id: {}", reservation.getId());
			} catch (Exception e) {
				log.error("Error charging payment on regular reservation for user with id: {} with error: ", reservation.getId(), e);
			}
		}
	}


}
