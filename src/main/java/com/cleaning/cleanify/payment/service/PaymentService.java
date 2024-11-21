package com.cleaning.cleanify.payment.service;

import com.cleaning.cleanify.auth.service.UserService;
import com.cleaning.cleanify.auth.model.User;
import com.cleaning.cleanify.auth.repository.UserRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.param.CustomerUpdateParams;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaymentService {
	private final UserRepository userRepository;
	private final UserService userService;


	public PaymentService(UserRepository userRepository, UserService userService) {
		this.userRepository = userRepository;
		this.userService = userService;
	}

	public Map<String, String> createPaymentIntent() throws StripeException {
		String customerId;
		User user = userService.getAuthenticatedUser();

		if (user.getCustomerId() == null) {
			customerId = createCustomer(user).getCustomerId();
		} else {
			customerId = user.getCustomerId();
		}
		try {
			// Створення SetupIntent
			Map<String, Object> params = new HashMap<>();
			params.put("customer", customerId);

			SetupIntent setupIntent = SetupIntent.create(params);

			// Підготовка відповіді
			Map<String, String> response = new HashMap<>();
			response.put("setupIntent", setupIntent.getClientSecret());
			response.put("customer", customerId);

			return response;

		} catch (StripeException e) {
			e.printStackTrace();
			throw new RuntimeException("Error creating setup intent: " + e.getMessage());
		}
	}


	public List<Map<String, String>> getPaimentMethods() {


		User user = userService.getAuthenticatedUser();

		if (user.getCustomerId() == null || user.getCustomerId().isEmpty()) {
			return List.of();
		}
		Map<String, Object> params = new HashMap<>();
		params.put("customer", user.getCustomerId());
		params.put("type", "card"); // Отримуємо тільки картки
		try {
			List<PaymentMethod> paymentMethods = PaymentMethod.list(params).getData();

			// Форматуємо відповіді
			return paymentMethods.stream().map(paymentMethod -> {
				Map<String, String> response = new HashMap<>();
				response.put("id", paymentMethod.getId());
				response.put("brand", paymentMethod.getCard().getBrand());
				response.put("last4", paymentMethod.getCard().getLast4());
				response.put("exp_month", String.valueOf(paymentMethod.getCard().getExpMonth()));
				response.put("exp_year", String.valueOf(paymentMethod.getCard().getExpYear()));
				return response;
			}).toList();

		} catch (StripeException e) {
			e.printStackTrace();
			throw new RuntimeException("Error fetching payment methods: " + e.getMessage());
		}
	}

	public void detachPaymentMethod(String methodId) {
		try {
			PaymentMethod paymentMethod = PaymentMethod.retrieve(methodId);
			paymentMethod.detach();
		} catch (StripeException e) {
			e.printStackTrace();
			throw new RuntimeException("Error detaching payment method: " + e.getMessage());
		}
	}

	public Map<String, Object> chargeCustomer(BigDecimal amount, String customerId) {
		String currency = "usd";

		try {
			// Retrieve the customer's default payment method
			Customer customer = Customer.retrieve(customerId);
			String paymentMethodId = customer.getInvoiceSettings().getDefaultPaymentMethod();
			if (paymentMethodId == null) {
				throw new RuntimeException("No default payment method on customer");
			}

			// Create a PaymentIntent to charge the customer
			Map<String, Object> paymentIntentParams = new HashMap<>();
			paymentIntentParams.put("amount", amount.multiply(BigDecimal.valueOf(100)).intValue());
			paymentIntentParams.put("currency", currency);
			paymentIntentParams.put("customer", customerId);
			paymentIntentParams.put("payment_method", paymentMethodId);
			paymentIntentParams.put("off_session", true);
			paymentIntentParams.put("confirm", true);

			PaymentIntent paymentIntent = PaymentIntent.create(paymentIntentParams);

			Map<String, Object> response = new HashMap<>();
			response.put("status", paymentIntent.getStatus());
			response.put("paymentIntentId", paymentIntent.getId());

			return response;

		} catch (StripeException e) {
			e.printStackTrace();

			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("error", e.getMessage());
			return errorResponse;
		}
	}

	public void setDefaultPaymentMethodForCustomer() throws StripeException {
		User user = userService.getAuthenticatedUser();
		String customerId = user.getCustomerId();

		// Отримуємо список платіжних методів клієнта
		Map<String, Object> params = new HashMap<>();
		params.put("customer", customerId);
		params.put("type", "card");

		PaymentMethodCollection paymentMethods = PaymentMethod.list(params);

		if (paymentMethods.getData().isEmpty()) {
			throw new RuntimeException("No payment methods found for customer");
		}

		// Отримуємо останній доданий платіжний метод
		PaymentMethod latestPaymentMethod = paymentMethods.getData().get(0);

		// Встановлюємо його як дефолтний
		Map<String, Object> updateParams = new HashMap<>();
		Map<String, Object> invoiceSettings = new HashMap<>();
		invoiceSettings.put("default_payment_method", latestPaymentMethod.getId());
		updateParams.put("invoice_settings", invoiceSettings);

		Customer customer = Customer.retrieve(customerId);
		customer.update(updateParams);
	}


	private User createCustomer(User user) throws StripeException {
		Map<String, Object> customerParams = new HashMap<>();
		customerParams.put("email", user.getEmail()); // Заміна на email користувача
		Customer customer = Customer.create(customerParams);
		user.setCustomerId(customer.getId());
		return userRepository.save(user);
	}
}
