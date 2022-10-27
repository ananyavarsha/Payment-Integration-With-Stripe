package com.example.demo;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

import com.stripe.Stripe;

@SpringBootApplication
@EnableAutoConfiguration
public class DemoApplication {

	@Value("${stripe.api.key}")
	private String stripeApiKey;
	
	@PostConstruct
	public void setUp() {
		Stripe.apiKey = stripeApiKey;
	}
	
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
