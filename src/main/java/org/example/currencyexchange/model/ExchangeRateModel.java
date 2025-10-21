package org.example.currencyexchange.model;

import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ExchangeRateModel {
    private int id;
    private final CurrencyModel baseCurrency;
    private final CurrencyModel targetCurrency;
    private final BigDecimal rate;

    public ExchangeRateModel(CurrencyModel baseCurrency, CurrencyModel targetCurrency, BigDecimal rate) {
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
    }

    public ExchangeRateModel(int id, CurrencyModel baseCurrency, CurrencyModel targetCurrency, BigDecimal rate) {
        this.id = id;
        this.baseCurrency = baseCurrency;
        this.targetCurrency = targetCurrency;
        this.rate = rate;
    }
}
