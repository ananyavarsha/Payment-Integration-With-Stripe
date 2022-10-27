package com.example.demo.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.demo.dto.UserDTO;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;

@Component
public class StripeUtil {

	@Value("${stripe.public.key}")
	private String stripePublicKey;
	
	public UserDTO getCustomer(String id) throws StripeException {
		Stripe.apiKey = stripePublicKey;

		Customer customer = Customer.retrieve(id);
		UserDTO data = setCustomerData(customer);
		return data;
	}
	
	public UserDTO setCustomerData(Customer customer) {
		UserDTO customerData = new UserDTO();
		customerData.setCustomerId(customer.getId());
		customerData.setName(customer.getName());
		customerData.setEmail(customer.getEmail());
		
		return customerData;
	}
	
}
