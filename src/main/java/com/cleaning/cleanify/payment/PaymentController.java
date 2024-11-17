package com.cleaning.cleanify.payment;

import com.stripe.exception.StripeException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {
	private final PaymentService paymentService;

	public PaymentController(PaymentService paymentService) {
		this.paymentService = paymentService;
	}

	@PostMapping
	public Map<String, String> createPaymentIntent() throws StripeException {
		return paymentService.createPaymentIntent();
	}
	@GetMapping
	public List<Map<String, String>> getPaymentMethods() {
		return paymentService.getPaimentMethods();
	}

	@DeleteMapping("/{methodId}")
	public void detachPaymentMethod(@PathVariable String methodId) {
		paymentService.detachPaymentMethod(methodId);
	}

	@PostMapping("/charge-customer")
	public Map<String, Object> chargeCustomer(@RequestBody Map<String, Object> request) {
		return paymentService.chargeCustomer(request);
	}

}
