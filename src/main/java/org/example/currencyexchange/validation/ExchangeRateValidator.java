package org.example.currencyexchange.validation;

import org.example.currencyexchange.dto.BaseExchangeCurrencyDTO;
import org.example.currencyexchange.exceptions.AppException;
import org.example.currencyexchange.exceptions.InvalidCurrencyException;

import java.math.BigDecimal;

public class ExchangeRateValidator {

    public static <T extends BaseExchangeCurrencyDTO> void validate(T dto) throws AppException {
        if (dto == null ||
                dto.getBaseCurrency() == null ||
                dto.getTargetCurrency() == null ||
                dto.getValue() == null) {
            throw new InvalidCurrencyException("All fields must be filled");
        }

        String base = dto.getBaseCurrency().trim().toUpperCase();
        String target = dto.getTargetCurrency().trim().toUpperCase();

        // Check currency codes (3 uppercase letters)
        if (!base.matches("[A-Z]{3}")) {
            throw new InvalidCurrencyException("Base currency code must be 3 uppercase letters");
        }

        if (!target.matches("[A-Z]{3}")) {
            throw new InvalidCurrencyException("Target currency code must be 3 uppercase letters");
        }

        // Validate rate (must be a positive BigDecimal)
        try {
            BigDecimal rate = new BigDecimal(dto.getValue());
            if (rate.compareTo(BigDecimal.ZERO) <= 0) {
                throw new InvalidCurrencyException("Value must be a positive number");
            }
        } catch (NumberFormatException e) {
            throw new InvalidCurrencyException("Value must be a valid number");
        }

        dto.setBaseCurrency(base);
        dto.setTargetCurrency(target);
    }
}
