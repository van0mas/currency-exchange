package org.example.currencyexchange.service;

import org.example.currencyexchange.dto.request.ExchangeRequestDTO;
import org.example.currencyexchange.dto.response.ExchangeResponseDTO;
import org.example.currencyexchange.exceptions.AppException;
import org.example.currencyexchange.exceptions.ExchangeRateNotFoundException;
import org.example.currencyexchange.model.CurrencyModel;
import org.example.currencyexchange.model.ExchangeRateModel;

import java.math.BigDecimal;
import java.util.Optional;

public class ExchangeService {

    private final ExchangeRateService exchangeService = new ExchangeRateService();
    private final CurrencyService currencyService = new CurrencyService();

    public ExchangeResponseDTO exchange(ExchangeRequestDTO dto) throws AppException {
        BigDecimal rate = findRateAnyWay(dto);
        BigDecimal amount = exchangeService.parseAndValidateVal(dto.getAmount());
        BigDecimal convertedAmount = amount.multiply(rate);

        ExchangeRateService.CurrencyPair pair = exchangeService.getCurrencyPair(dto);
        CurrencyModel baseCurrency = pair.baseCurrency();
        CurrencyModel targetCurrency = pair.targetCurrency();

        ExchangeResponseDTO responseDTO = new ExchangeResponseDTO();
        responseDTO.setBaseCurrency(baseCurrency);
        responseDTO.setTargetCurrency(targetCurrency);
        responseDTO.setRate(rate);
        responseDTO.setAmount(amount);
        responseDTO.setConvertedAmount(convertedAmount);

        return responseDTO;
    }

    // Finds the exchange rate for the given currency pair in any possible way.
    private BigDecimal findRateAnyWay(ExchangeRequestDTO dto) throws AppException {
        ExchangeRateService.CurrencyPair currencyPair = exchangeService.getCurrencyPair(dto);
        CurrencyModel baseCurrency = currencyPair.baseCurrency();
        CurrencyModel targetCurrency = currencyPair.targetCurrency();

        // If same currency, return 1
        if (baseCurrency.equals(targetCurrency)) {
            return BigDecimal.ONE;
        }

        // Find exchange rate in any direction
        Optional<ExchangeRateModel> rateOpt = exchangeService.findExchangeRateOpt(dto);
        if (rateOpt.isPresent()) {
            return rateOpt.get().getRate();
        }

        // Try to find via USD
        CurrencyModel usd = currencyService.getCurrencyModelByCode("USD");
        Optional<ExchangeRateModel> usdToBase = exchangeService.findExchangeRateOpt(usd, baseCurrency);
        Optional<ExchangeRateModel> usdToTarget = exchangeService.findExchangeRateOpt(usd, targetCurrency);

        if (usdToBase.isPresent() && usdToTarget.isPresent()) {
            BigDecimal usdToBaseRate = usdToBase.get().getRate();
            BigDecimal usdToTargetRate = usdToTarget.get().getRate();

            return usdToTargetRate.divide(usdToBaseRate, 10, BigDecimal.ROUND_HALF_UP);
        }

        throw new ExchangeRateNotFoundException("No exchange rate found for the given currency pair");
    }
}
