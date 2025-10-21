package org.example.currencyexchange.service;

import org.example.currencyexchange.dao.CurrencyDAO;
import org.example.currencyexchange.dto.request.CurrencyRequestDTO;
import org.example.currencyexchange.dto.response.CurrencyResponseDTO;
import org.example.currencyexchange.exceptions.*;
import org.example.currencyexchange.mapper.dao.CurrencyResponseMapper;
import org.example.currencyexchange.mapper.model.CurrencyDTOMapper;
import org.example.currencyexchange.model.CurrencyModel;

import java.util.List;

public class CurrencyService {
    private final CurrencyDAO currencyDAO = new CurrencyDAO();

    public List<CurrencyResponseDTO> getCurrencies() throws AppException {
        return CurrencyResponseMapper.from(currencyDAO.getCurrencies());
    }

    public CurrencyResponseDTO addCurrency(CurrencyRequestDTO currency) throws AppException {
        CurrencyModel model = CurrencyDTOMapper.from(currency);
        return CurrencyResponseMapper.from(currencyDAO.save(model));
    }

    // метод для сервиса
    public CurrencyModel getCurrencyModelByCode(String code) throws AppException {
        return currencyDAO.getCurrency(code)
                .orElseThrow(() -> new CurrencyNotFoundException("Currency with code " + code + " does not exist"));
    }

    // метод для сервлета
    public CurrencyResponseDTO getCurrencyByCode(String code) throws AppException {
        return CurrencyResponseMapper.from(getCurrencyModelByCode(code));
    }
}
