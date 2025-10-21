package org.example.currencyexchange.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.currencyexchange.model.CurrencyModel;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class ExchangeRateResponseDTO {
    private int id;
    private CurrencyModel baseCurrency;
    private CurrencyModel targetCurrency;
    private BigDecimal rate;
}
