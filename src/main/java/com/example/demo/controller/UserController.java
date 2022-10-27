package com.example.demo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.UserDTO;
import com.example.demo.model.User;
import com.example.demo.util.StripeUtil;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.CustomerCollection;

@RestController
public class UserController {

	@Value("${stripe.public.key}")
	private String stripePublicKey;
	
	@Autowired
	private StripeUtil stripeUtil;
	
	@PostMapping("/createCustomer")
	public User createCustomer(@RequestBody UserDTO userDTO) throws StripeException {
		Stripe.apiKey = stripePublicKey;
		ModelMapper modelMapper = new ModelMapper();
		User user = modelMapper.map(userDTO, User.class);
		Map<String, Object> params = new HashMap<>();
		params.put("name", user.getName());
		params.put("email", user.getEmail());
		Customer customer = Customer.create(params);
		user.setCustomerId(customer.getId());
		return user;
	}
	
	@GetMapping("/getAllCustomer")
	public List<UserDTO> getAllCustomer() throws StripeException {
		Stripe.apiKey = stripePublicKey;

		Map<String, Object> params = new HashMap<>();
		params.put("limit", 3);

		CustomerCollection customers = Customer.list(params);
		List<UserDTO> allCustomer = new ArrayList<UserDTO>();
		for (int i = 0; i < customers.getData().size(); i++) {
			UserDTO customerData = new UserDTO();
			customerData.setCustomerId(customers.getData().get(i).getId());
			customerData.setName(customers.getData().get(i).getName());
			customerData.setEmail(customers.getData().get(i).getEmail());
			allCustomer.add(customerData);
		}
		return allCustomer;
	}
	
	@DeleteMapping("/deleteCustomer/{id}")
	public String deleteCustomer(@PathVariable("id") String id) throws StripeException {
		Stripe.apiKey = stripePublicKey;
		Customer customer = Customer.retrieve(id);
		Customer deletedCustomer = customer.delete();
		return "successfully deleted";
	}
	
	@GetMapping("/getCustomer/{id}")
	public UserDTO getCustomer(@PathVariable("id") String id) throws StripeException {
		
		UserDTO output =stripeUtil.getCustomer(id);
		return output;
	}
}
