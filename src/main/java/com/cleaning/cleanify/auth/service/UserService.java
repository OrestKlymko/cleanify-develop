package com.cleaning.cleanify.auth.service;

import com.cleaning.cleanify.auth.dto.UserUpdateRequest;
import com.cleaning.cleanify.auth.dto.UserUpdateResponse;
import com.cleaning.cleanify.auth.model.Role;
import com.cleaning.cleanify.auth.model.User;
import com.cleaning.cleanify.auth.repository.UserRepository;

import com.cleaning.cleanify.mail.MailService;
import com.cleaning.cleanify.payment.service.PaymentService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentMethod;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final MailService mailService;
	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(UserService.class);

	public UserService(UserRepository userRepository, MailService mailService) {
		this.userRepository = userRepository;
		this.mailService = mailService;
	}


	public User getUserByEmail(String email) {
		return userRepository.findByEmail(email).orElseThrow();
	}

	public User getAuthenticatedUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String idUsers = authentication.getName();
		return userRepository.findById(Long.valueOf(idUsers)).orElseThrow();
	}

	public UserUpdateResponse getUserInfo() {
		User authenticatedUser = getAuthenticatedUser();
		return new UserUpdateResponse(
				authenticatedUser.getEmail(),
				authenticatedUser.getFirstName(),
				authenticatedUser.getBirthDate(),
				authenticatedUser.getPhoneNumber(),
				authenticatedUser.getAddress()
		);
	}

	public User saveUser(String email, GoogleIdToken.Payload tokenPayload) {
		return userRepository.findByEmail(email).orElseGet(() -> {
			String givenName = (String) tokenPayload.get("given_name");
			User user = new User();
			user.setEmail(email);
			user.setFirstName(givenName);
			if (email.equals("cleanifybee@gmail.com")) {
				user.setRole(Role.ADMIN);
			} else {
				user.setRole(Role.USER);
			}
			return userRepository.save(user);
		});
	}

	public User saveUser(String email) {
		return userRepository.findByEmail(email).orElseGet(() -> {
			User user = new User();
			user.setEmail(email);
			user.setRole(Role.USER);
			return userRepository.save(user);
		});
	}

	public UserUpdateResponse updateUser(UserUpdateRequest userRequest) {
		User user = getAuthenticatedUser();
		user.setFirstName(userRequest.givenName());
		user.setBirthDate(userRequest.birthDate());
		user.setPhoneNumber(userRequest.phone());
		user.setAddress(userRequest.address());
		User savedUser = userRepository.save(user);
		return new UserUpdateResponse(
				savedUser.getEmail(),
				savedUser.getFirstName(),
				savedUser.getBirthDate(),
				savedUser.getPhoneNumber(),
				savedUser.getAddress()
		);
	}

	@Transactional
	public void deleteUser() {
		User user = getAuthenticatedUser();
		String customerId = user.getCustomerId();
		deleteStripeCustomer(customerId);
		userRepository.delete(user);
		mailService.successDeleteProfileUser(user.getEmail(), user);
	}

	private void detachAllPaymentMethods(String customerId) throws StripeException {
		List<PaymentMethod> paymentMethods = PaymentMethod.list(Map.of("customer", customerId)).getData();
		for (PaymentMethod paymentMethod : paymentMethods) {
			try {
				paymentMethod.detach();
			} catch (Exception e) {
				logger.error("Failed to detach payment method: " + paymentMethod.getId(), e);
			}
		}
	}

	public void deleteStripeCustomer(String customerId) {
		try {
			Customer customer = Customer.retrieve(customerId);
			detachAllPaymentMethods(customerId);
			customer.delete();
		} catch (Exception e) {
			throw new RuntimeException("Failed to delete Stripe customer.");
		}
	}
}
