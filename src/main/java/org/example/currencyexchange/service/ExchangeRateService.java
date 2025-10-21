package org.example.currencyexchange.service;

import org.example.currencyexchange.dao.ExchangeRateDAO;
import org.example.currencyexchange.dto.BaseExchangeCurrencyDTO;
import org.example.currencyexchange.dto.request.ExchangeRateRequestDTO;
import org.example.currencyexchange.dto.request.ExchangeRequestDTO;
import org.example.currencyexchange.dto.response.ExchangeRateResponseDTO;
import org.example.currencyexchange.exceptions.*;
import org.example.currencyexchange.mapper.dao.ExchangeResponseMapper;
import org.example.currencyexchange.model.CurrencyModel;
import org.example.currencyexchange.model.ExchangeRateModel;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class ExchangeRateService {

    private static final BigDecimal MAX_VALUE = new BigDecimal("99999999999999999999");
    private final ExchangeRateDAO exchangeRateDAO = new ExchangeRateDAO();
    private final CurrencyService currencyService = new CurrencyService();

    public record CurrencyPair(CurrencyModel baseCurrency, CurrencyModel targetCurrency) {}

    public List<ExchangeRateResponseDTO> getExchangeRates() throws AppException {
        return ExchangeResponseMapper.from(exchangeRateDAO.getExchangeRates());
    }

    public ExchangeRateResponseDTO addExchangeRate(ExchangeRateRequestDTO dto) throws AppException {
        return ExchangeResponseMapper.from(exchangeRateDAO.save(buildExchangeRateModel(dto)));
    }

    public ExchangeRateResponseDTO updateExchangeRate(ExchangeRateRequestDTO dto) throws AppException {
        return ExchangeResponseMapper.from(exchangeRateDAO.update(buildExchangeRateModel(dto)));
    }

    public ExchangeRateResponseDTO getExchangeRateByCode(ExchangeRateRequestDTO dto) throws AppException {

        CurrencyPair pair = getCurrencyPair(dto);
        ExchangeRateModel model = exchangeRateDAO.getExchangeRate(pair.baseCurrency, pair.targetCurrency)
                .orElseThrow(() -> new ExchangeRateNotFoundException(
                        "exchange rate with codes " +
                                pair.baseCurrency.getCode() +
                                " and " +
                                pair.targetCurrency.getCode()
                                + " does not exist")
                );
        return ExchangeResponseMapper.from(model);
    }

    public Optional<ExchangeRateModel> findExchangeRateOpt(ExchangeRequestDTO dto) throws AppException {
        CurrencyPair pair = getCurrencyPair(dto);
        return findExchangeRateOpt(pair.baseCurrency, pair.targetCurrency);
    }

    public Optional<ExchangeRateModel> findExchangeRateOpt(CurrencyModel baseCurrency, CurrencyModel targetCurrency) throws AppException {

        Optional<ExchangeRateModel> direct = exchangeRateDAO.getExchangeRate(baseCurrency, targetCurrency);
        return direct.isPresent()
                ? direct
                : exchangeRateDAO.getExchangeRate(targetCurrency, baseCurrency)
                .map(r -> new ExchangeRateModel(
                        r.getBaseCurrency(),
                        r.getTargetCurrency(),
                        BigDecimal.ONE.divide(r.getRate(), 10, BigDecimal.ROUND_HALF_UP)
                ));
    }

    public BigDecimal parseAndValidateVal(String rate) throws AppException {
        BigDecimal rateValue = new BigDecimal(rate);

        if (rateValue.compareTo(MAX_VALUE) >= 0) {
            throw new InvalidCurrencyException("value must be less than " + MAX_VALUE);
        }
        return rateValue;
    }

    public <T extends BaseExchangeCurrencyDTO> CurrencyPair getCurrencyPair(T dto) throws AppException {
        CurrencyModel baseCurrency = currencyService.getCurrencyModelByCode(dto.getBaseCurrency());
        CurrencyModel targetCurrency = currencyService.getCurrencyModelByCode(dto.getTargetCurrency());
        return new CurrencyPair(baseCurrency, targetCurrency);
    }

    private ExchangeRateModel buildExchangeRateModel(ExchangeRateRequestDTO dto) throws AppException {
        if (dto.getBaseCurrency().equals(dto.getTargetCurrency())) {
            throw new InvalidCurrencyException("Currencies must be different");
        }
        BigDecimal rate = parseAndValidateVal(dto.getRate());
        CurrencyPair pair = getCurrencyPair(dto);
        return new ExchangeRateModel(pair.baseCurrency, pair.targetCurrency, rate);
    }
}
