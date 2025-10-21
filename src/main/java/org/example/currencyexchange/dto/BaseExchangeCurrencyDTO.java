package org.example.currencyexchange.dto;

public interface BaseExchangeCurrencyDTO {

    String getBaseCurrency();
    String getTargetCurrency();
    String getValue();
    void setBaseCurrency(String baseCurrency);
    void setTargetCurrency(String targetCurrency);
}
