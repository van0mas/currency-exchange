package org.example.currencyexchange.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.currencyexchange.dto.BaseExchangeCurrencyDTO;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRateRequestDTO implements BaseExchangeCurrencyDTO {
    private String baseCurrency;
    private String targetCurrency;
    private String rate;

    @Override
    public String getValue() {
        return rate;
    }
}
