package org.example.currencyexchange.exceptions;

import javax.servlet.http.HttpServletResponse;

public class DAOException extends AppException {
    public DAOException(String message) {
        super(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message);
    }
}
