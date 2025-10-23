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

        if (!ValidationUtils.isValidCurrencyPath(pathInfo) || !ValidationUtils.isValidCurrencyPairPath(pathInfo)) {
            // return empty object
            return new ExchangeRateRequestDTO();
        }

        String baseCurrency = pathInfo.substring(1, 4);
        String targetCurrency = pathInfo.substring(4);

        String rateValue = rate.length > 0 ? rate[0] : null;

        return new ExchangeRateRequestDTO(baseCurrency, targetCurrency, rateValue);
    }
}
