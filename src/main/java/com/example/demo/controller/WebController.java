package com.example.demo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.dto.UserDTO;
import com.example.demo.form.CheckoutForm;
import com.example.demo.util.StripeUtil;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.CustomerCollection;

@Controller
public class WebController {

	@Value("${stripe.public.key}")
	private String stripePublicKey;
	
	@Autowired
	private StripeUtil stripeUtil;
	
	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("checkoutForm",new CheckoutForm());
		return "index";
	}
	
	@PostMapping("/")
	public String checkout(@ModelAttribute @Valid CheckoutForm checkoutForm,BindingResult bindingResult, Model model) {
		if(bindingResult.hasErrors()) {
			return "index";
		}
		model.addAttribute("stripePublicKey",stripePublicKey);
		model.addAttribute("amount",checkoutForm.getAmount());
		model.addAttribute("email",checkoutForm.getEmail());
		return "checkout";
	}
	
	
	@GetMapping("/success")
	public String success() {
		return "success";
	}
	
	@RequestMapping("/user")
	public String user(Model model) throws StripeException {
		
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
		model.addAttribute("customers", allCustomer);
		return "user";
	}
	
	@RequestMapping("/createCustomer")
	public String createCustomer(UserDTO customerData) {
		return "create-customer";
	}
	
	@RequestMapping("/addCustomer")
	public String addCustomer(UserDTO customerData) throws StripeException {
		
		Stripe.apiKey = stripePublicKey;
		Map<String, Object> params = new HashMap<>();
		params.put("name", customerData.getName());
		params.put("email", customerData.getEmail());
		Customer customer = Customer.create(params);
		
		return "success-user";
	}
	
	@RequestMapping("/updateCustomer")
	public String updateCustomer() throws StripeException {
		return "update-customer";
	}
	
	@RequestMapping("/deleteCustomer/{id}")
	public String deleteCustomer(@PathVariable("id") String id) throws StripeException {
		Stripe.apiKey = stripePublicKey;
		Customer customer = Customer.retrieve(id);
		Customer deletedCustomer = customer.delete();
		return "success-user";
	}

	@RequestMapping("/getCustomer/{id}")
	public String getCustomer(@PathVariable("id") String id, Model model) throws StripeException {
		Stripe.apiKey = stripePublicKey;
		UserDTO output =stripeUtil.getCustomer(id);
		model.addAttribute("customerData", output);
		return "update-customer";
	}
}
