package com.joeCurrencyExchange.joeCurrencyExchange.config;

import com.joeCurrencyExchange.joeCurrencyExchange.service.ExchangeRateService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StartupRunner {

    @Bean
    public CommandLineRunner runOnStartup(ExchangeRateService exchangeRateService) {
        return args -> {
            // Fetch rates at startup
            exchangeRateService.fetchAndStoreRates();
        };
    }
}
