package org.example.currencyexchange.exceptions;

public class AppException extends Exception {
    private final int statusCode;

    public AppException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
