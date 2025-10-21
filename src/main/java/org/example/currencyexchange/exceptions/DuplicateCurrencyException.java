package org.example.currencyexchange.exceptions;

import javax.servlet.http.HttpServletResponse;

public class DuplicateCurrencyException extends AppException {
    public DuplicateCurrencyException(String message) {
        super(HttpServletResponse.SC_CONFLICT, message);
    }
}
