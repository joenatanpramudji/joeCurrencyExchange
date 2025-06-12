package com.joeCurrencyExchange.joeCurrencyExchange.service;

import com.joeCurrencyExchange.joeCurrencyExchange.dto.ExchangeRateResponse;
import com.joeCurrencyExchange.joeCurrencyExchange.model.ExchangeRate;
import com.joeCurrencyExchange.joeCurrencyExchange.repository.ExchangeRateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class ExchangeRateService {
    private static final Logger logger = LoggerFactory.getLogger(ExchangeRateService.class);

    private final RestTemplate restTemplate;
    private final ExchangeRateRepository repository;

    /** e.g. https://api.currencylayer.com/live */
    @Value("${exchange.api.base-url}")
    private String externalApiBaseUrl;

    /** your CurrencyLayer access key */
    @Value("${exchange.api.key}")
    private String apiKey;

    public ExchangeRateService(RestTemplate restTemplate,
                               ExchangeRateRepository repository) {
        this.restTemplate = restTemplate;
        this.repository   = repository;
    }

    /**
     * Fetches live rates from CurrencyLayer and upserts into DB.
     */
    @Transactional
    public void fetchAndStoreRates() {
        // Build URL: e.g. https://api.currencylayer.com/live?access_key=KEY
        String url = UriComponentsBuilder
                .fromHttpUrl(externalApiBaseUrl)
                .queryParam("access_key", apiKey)
                .toUriString();

        // 1) Fetch and map to your DTO
        ExchangeRateResponse resp = restTemplate
                .getForObject(url, ExchangeRateResponse.class);

        if (resp == null || resp.getQuotes() == null || resp.getQuotes().isEmpty()) {
            logger.warn("No quotes returned from URL={}", url);
            return;
        }

        String baseCurrency = resp.getSource();           // e.g. "USD"
        LocalDateTime now   = LocalDateTime.now();

        // 2) Iterate quotes like "USDEUR":0.87494
        for (Map.Entry<String, BigDecimal> entry : resp.getQuotes().entrySet()) {
            String pairKey = entry.getKey();               // e.g. "USDEUR"
            BigDecimal rate = entry.getValue();

            // strip the base prefix to get target currency, e.g. "EUR"
            String targetCurrency = pairKey.substring(baseCurrency.length());

            // upsert into DB
            repository.findByBaseCurrencyAndTargetCurrency(baseCurrency, targetCurrency)
                    .map(existing -> {
                        existing.setRate(rate);
                        existing.setLastUpdated(now);
                        return repository.save(existing);
                    })
                    .orElseGet(() -> repository.save(
                            new ExchangeRate(baseCurrency, targetCurrency, rate, now)
                    ));
        }

        logger.info("Upserted {} quote pairs from {}", resp.getQuotes().size(), url);
    }

    /**
     * Read-only: get a single rate from DB.
     */
    @Transactional(readOnly = true)
    public BigDecimal getRate(String baseCurrency,
                              String targetCurrency) {
        return repository
                .findByBaseCurrencyAndTargetCurrency(baseCurrency, targetCurrency)
                .map(ExchangeRate::getRate)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No rate for " + baseCurrency + "→" + targetCurrency));
    }

    /**
     * Convert amount using stored rate.
     */
    @Transactional(readOnly = true)
    public BigDecimal convertAmount(String baseCurrency,
                                    String targetCurrency,
                                    BigDecimal amount) {
        BigDecimal rate = getRate(baseCurrency, targetCurrency);
        return amount.multiply(rate);
    }
}