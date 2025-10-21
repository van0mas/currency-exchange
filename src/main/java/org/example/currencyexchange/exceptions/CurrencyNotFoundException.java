package org.example.currencyexchange.exceptions;

import javax.servlet.http.HttpServletResponse;

public class CurrencyNotFoundException extends AppException {
    public CurrencyNotFoundException(String message) {
        super(HttpServletResponse.SC_NOT_FOUND, message);
    }
}
