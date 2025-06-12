package com.joeCurrencyExchange.joeCurrencyExchange.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "exchange_rate")
public class ExchangeRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "base_currency", nullable = false, length = 3)
    private String baseCurrency; // e.g., "USD"

    @Column(name = "target_currency", nullable = false, length = 3)
    private String targetCurrency; // e.g., "EUR"

    @Column(nullable = false, precision = 19, scale = 8)
    private BigDecimal rate; // e.g., 0.9423

    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated;

    // Constructors, getters, setters (Lombok can help if you add @Data, @NoArgsConstructor, @AllArgsConstructor)
    public ExchangeRate() {}

    public ExchangeRate(String baseCurrency, String targetCurrency, BigDecimal rate, LocalDateTime lastUpdated) {
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
        this.lastUpdated = lastUpdated;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getBaseCurrency() { return baseCurrency; }
    public void setBaseCurrency(String baseCurrency) { this.baseCurrency = baseCurrency; }

    public String getTargetCurrency() { return targetCurrency; }
    public void setTargetCurrency(String targetCurrency) { this.targetCurrency = targetCurrency; }

    public BigDecimal getRate() { return rate; }
    public void setRate(BigDecimal rate) { this.rate = rate; }

    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
}
