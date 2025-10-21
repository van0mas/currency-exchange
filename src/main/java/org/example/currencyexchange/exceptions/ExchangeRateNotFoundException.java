package org.example.currencyexchange.exceptions;

import javax.servlet.http.HttpServletResponse;

public class ExchangeRateNotFoundException extends AppException {
    public ExchangeRateNotFoundException(String message) {
        super(HttpServletResponse.SC_NOT_FOUND, message);
    }
}
