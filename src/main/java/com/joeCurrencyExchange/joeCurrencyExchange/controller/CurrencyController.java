package com.joeCurrencyExchange.joeCurrencyExchange.controller;

import com.joeCurrencyExchange.joeCurrencyExchange.service.ExchangeRateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CurrencyController {

    private final ExchangeRateService exchangeRateService;

    public CurrencyController(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }

    /**
     * Example: GET /api/convert?from=USD&to=EUR&amount=100
     */
    @GetMapping("/convert")
    public ResponseEntity<Map<String, Object>> convert(
            @RequestParam("from") String base,
            @RequestParam("to") String target,
            @RequestParam("amount") BigDecimal amount
    ) {
        // Validate currency codes: You might enforce uppercase, length 3, etc.
        base = base.toUpperCase();
        target = target.toUpperCase();

        BigDecimal rate = exchangeRateService.getRate(base, target);
        BigDecimal converted = exchangeRateService.convertAmount(base, target, amount);

        Map<String, Object> response = new HashMap<>();
        response.put("base", base);
        response.put("target", target);
        response.put("amount", amount);
        response.put("rate", rate);
        response.put("converted", converted);

        return ResponseEntity.ok(response);
    }
}
