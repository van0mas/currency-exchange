package org.example.currencyexchange.mapper.request;

import org.example.currencyexchange.dto.request.ExchangeRateRequestDTO;
import org.example.currencyexchange.exceptions.AppException;
import org.example.currencyexchange.exceptions.CurrencyNotFoundException;
import org.example.currencyexchange.exceptions.InvalidCurrencyException;
import org.example.currencyexchange.validation.ValidationUtils;

import javax.servlet.http.HttpServletRequest;

public class ExchangeRateRequestMapper {

    public static ExchangeRateRequestDTO from(HttpServletRequest request) {
        String baseCurrency = request.getParameter("baseCurrencyCode");
        String targetCurrency = request.getParameter("targetCurrencyCode");
        String rate = request.getParameter("rate");

        return new ExchangeRateRequestDTO(baseCurrency, targetCurrency, rate);
    }

    public static ExchangeRateRequestDTO from(String pathInfo) throws AppException {
        return parsePath(pathInfo);
    }

    public static ExchangeRateRequestDTO from(String pathInfo, String rate) throws AppException {
        return parsePath(pathInfo, rate);
    }

    private static ExchangeRateRequestDTO parsePath(String pathInfo, String... rate) {

        if (pathInfo == null || pathInfo.length() < 2) {
            // return empty object
            return new ExchangeRateRequestDTO();
        }

        pathInfo = pathInfo.startsWith("/") ? pathInfo.substring(1) : pathInfo;

        String baseCurrency = "";
        String targetCurrency = "";

        if (pathInfo.length() >= 3) {
            baseCurrency = pathInfo.substring(0, 3);
        }

        if (pathInfo.length() > 3) {
            // take next 3 chars for target (avoid IndexOutOfBounds)
            targetCurrency = pathInfo.substring(3, Math.min(6, pathInfo.length()));
        }

        String rateValue = rate.length > 0 ? rate[0] : null;

        return new ExchangeRateRequestDTO(baseCurrency, targetCurrency, rateValue);
    }
}
