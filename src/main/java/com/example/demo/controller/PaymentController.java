package com.example.demo.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CreatePayment;
import com.example.demo.dto.CreatePaymentResponse;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

@RestController
public class PaymentController {
	
	@PostMapping("/create-payment-intent")
	public CreatePaymentResponse createPaymentIntent(@RequestBody @Valid CreatePayment createPayment) throws StripeException {
		
	      PaymentIntentCreateParams params =
	        PaymentIntentCreateParams.builder()
	          .setAmount(createPayment.getAmount() * 100L)
	          .setCurrency("cad")
	          .build();

	      // Create a PaymentIntent with the order amount and currency
	      PaymentIntent paymentIntent = PaymentIntent.create(params);

	      return new CreatePaymentResponse(paymentIntent.getClientSecret());
	}
}
