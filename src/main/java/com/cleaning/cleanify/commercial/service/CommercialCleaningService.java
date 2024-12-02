package com.cleaning.cleanify.commercial.service;


import com.cleaning.cleanify.auth.model.User;
import com.cleaning.cleanify.auth.service.UserService;
import com.cleaning.cleanify.commercial.dto.AllCommercialResponse;
import com.cleaning.cleanify.commercial.dto.ConfirmationRequest;
import com.cleaning.cleanify.commercial.dto.CreateCommercialCleaning;
import com.cleaning.cleanify.commercial.model.CommercialCleaning;
import com.cleaning.cleanify.commercial.repository.CommercialCleaningRepository;
import com.cleaning.cleanify.mail.MailService;
import com.cleaning.cleanify.payment.service.PaymentService;
import com.cleaning.cleanify.reservation.model.State;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommercialCleaningService {
	private final CommercialCleaningRepository commercialCleaningRepository;
	private final UserService userService;
	private final MailService mailService;
	private final PaymentService paymentService;

	public CommercialCleaningService(CommercialCleaningRepository commercialCleaningRepository, UserService userService, MailService mailService, PaymentService paymentService) {
		this.commercialCleaningRepository = commercialCleaningRepository;
		this.userService = userService;
		this.mailService = mailService;
		this.paymentService = paymentService;
	}


	public List<AllCommercialResponse> getCommercialCleaning() {
		return commercialCleaningRepository.getAllCommercialCleaning();
	}

	public List<AllCommercialResponse> getCommercialCleaningByUser() {
		User user = userService.getAuthenticatedUser();
		return commercialCleaningRepository.getAllCommercialCleaningByUser(user.getId());
	}

	@Transactional
	public void createCommercialCleaning(CreateCommercialCleaning createCommercialCleaning) {
		CommercialCleaning commercialCleaning = new CommercialCleaning();
		User user = userService.getAuthenticatedUser();
		commercialCleaning.setEmail(user.getEmail());
		commercialCleaning.setState(State.NEW);
		commercialCleaning.setContactPersonName(createCommercialCleaning.contactPersonName());
		commercialCleaning.setAddress(createCommercialCleaning.address());
		commercialCleaning.setFloor(createCommercialCleaning.floor());
		commercialCleaning.setSquareFt(createCommercialCleaning.squareFootage());
		commercialCleaning.setCompanyName(createCommercialCleaning.companyName());
		commercialCleaning.setPhoneNumber(createCommercialCleaning.contactPhoneNumber());
		commercialCleaning.setDateTime(createCommercialCleaning.date());
		commercialCleaning.setAdditionalInstructions(createCommercialCleaning.additionalInstructions());
		commercialCleaning.setUser(user);
		commercialCleaningRepository.save(commercialCleaning);
		mailService.sendConfirmationOfCompanyCleaningRequestToCall(user.getEmail(), user);
	}


	@Transactional
	public void confirmCommercialCleaning(ConfirmationRequest request) {
		CommercialCleaning commercialCleaning = commercialCleaningRepository.findById(request.id()).orElseThrow();
		commercialCleaning.setState(State.ACCEPTED);
		commercialCleaning.setAdditionalInstructions(request.additionalInstructions());
		commercialCleaning.setPrice(request.price());
		paymentService.chargeCustomer(request.price(), commercialCleaning.getUser().getCustomerId());
		mailService.sendConfirmationCleaningCommercialBuilding(commercialCleaning.getEmail(), userService.getAuthenticatedUser());
	}

	@Transactional
	public void changeDateOfCommercialCleaning(Long id, LocalDateTime date) {
		CommercialCleaning commercialCleaning = commercialCleaningRepository.findById(id).orElseThrow();
		commercialCleaning.setDateTime(date);
		commercialCleaningRepository.save(commercialCleaning);
		mailService.changeTimeOfCommercialCleaning(commercialCleaning.getEmail(), userService.getAuthenticatedUser());
	}

	@Transactional
	public void cancelCommercialCleaning(Long id) {
		CommercialCleaning commercialCleaning = commercialCleaningRepository.findById(id).orElseThrow();
		commercialCleaning.setState(State.CANCELLED);
		commercialCleaningRepository.save(commercialCleaning);
		mailService.cancelCommercialCleaning(commercialCleaning.getEmail(), userService.getAuthenticatedUser());
	}
}
