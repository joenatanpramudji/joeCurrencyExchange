package com.joeCurrencyExchange.joeCurrencyExchange.scheduler;

import com.joeCurrencyExchange.joeCurrencyExchange.service.ExchangeRateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ExchangeRateScheduler {
    private static final Logger logger = LoggerFactory.getLogger(ExchangeRateScheduler.class);
    private final ExchangeRateService exchangeRateService;

    public ExchangeRateScheduler(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    /**
     * Every hour on the hour (e.g., 10:00, 11:00, ...), fetch rates for USD.
     * You can add more base currencies if desired.
     */
    @Scheduled(cron = "0 0 * * * *") // (second, minute, hour, day, month, day-of-week)
    public void scheduledRateUpdate() {
        logger.info("Scheduled job starting: fetch latest USD→XYZ rates");
        exchangeRateService.fetchAndStoreRates();
    }
}
