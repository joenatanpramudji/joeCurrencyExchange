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

    private final ExchangeRateService svc;

    public CurrencyController(ExchangeRateService svc) {
        this.svc = svc;
    }

    @GetMapping("/convert")
    public ResponseEntity<Map<String,Object>> convert(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam BigDecimal amount
    ) {
        from = from.toUpperCase();
        to   = to.toUpperCase();

        // derive the unit rate, then the converted amount
        BigDecimal rate      = svc.getDerivedRate(from, to);
        BigDecimal converted = svc.convertAmount(from, to, amount);

        Map<String,Object> resp = Map.of(
                "base",      from,
                "target",    to,
                "amount",    amount,
                "rate",      rate,
                "converted", converted
        );
        return ResponseEntity.ok(resp);
    }
}
