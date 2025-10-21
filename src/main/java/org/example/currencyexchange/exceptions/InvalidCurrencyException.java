package org.example.currencyexchange.exceptions;

import javax.servlet.http.HttpServletResponse;

public class InvalidCurrencyException extends AppException {
    public InvalidCurrencyException(String message) {
        super(HttpServletResponse.SC_BAD_REQUEST, message);
    }
}
