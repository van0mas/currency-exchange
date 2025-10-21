package org.example.currencyexchange.validation;

import org.example.currencyexchange.dto.request.CurrencyRequestDTO;
import org.example.currencyexchange.exceptions.AppException;
import org.example.currencyexchange.exceptions.InvalidCurrencyException;

public class CurrencyValidator {

    public static void validate(CurrencyRequestDTO dto) throws AppException {
        if (dto == null ||
                dto.getCode() == null || dto.getName() == null || dto.getSign() == null ||
                dto.getCode().trim().isEmpty() || dto.getName().trim().isEmpty() || dto.getSign().trim().isEmpty()) {
            throw new InvalidCurrencyException("Currency fields cannot be null or empty");
        }

        // Field length checks
        if (dto.getCode().length() != 3 || dto.getName().length() > 25 || dto.getSign().length() > 5) {
            throw new InvalidCurrencyException("Currency fields have invalid length");
        }

        // Code must be exactly 3 uppercase letters
        if (!dto.getCode().matches("[A-Z]{3}")) {
            throw new InvalidCurrencyException("Currency code must contain exactly 3 uppercase letters");
        }

        // Sign must contain no more than 4 characters
        if (dto.getSign().codePointCount(0, dto.getSign().length()) > 4) {
            throw new InvalidCurrencyException("Sign code must contain less than 5 characters");
        }

        // Sign allows all Unicode symbols except dangerous ones (e.g., <, >, &, ', etc.)
        if (!dto.getSign().matches("[\\p{L}\\p{N}\\p{P}\\p{S}&&[^<>\"'&]]")) {
            throw new InvalidCurrencyException("Currency sign contains invalid characters");
        }

        // Full name allows only letters, spaces, dash, and underscore
        if (!dto.getName().matches("[\\p{L} \\-_]+")) {
            throw new InvalidCurrencyException("Currency full name contains invalid characters");
        }
    }
}
