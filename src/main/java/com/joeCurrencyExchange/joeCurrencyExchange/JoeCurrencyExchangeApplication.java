package com.joeCurrencyExchange.joeCurrencyExchange;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class JoeCurrencyExchangeApplication {

	public static void main(String[] args) {
		SpringApplication.run(JoeCurrencyExchangeApplication.class, args);
	}

}
