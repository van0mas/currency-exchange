package org.example.currencyexchange.mapper.dao;

import org.example.currencyexchange.dto.response.ExchangeRateResponseDTO;
import org.example.currencyexchange.model.ExchangeRateModel;

import java.util.List;
import java.util.stream.Collectors;

public class ExchangeResponseMapper {
    public static ExchangeRateResponseDTO from(ExchangeRateModel model) {
        return new ExchangeRateResponseDTO(
                model.getId(),
                model.getBaseCurrency(),
                model.getTargetCurrency(),
                model.getRate()
        );
    }

    public static List<ExchangeRateResponseDTO> from(List<ExchangeRateModel> list) {
        return list.stream()
                .map(ExchangeResponseMapper::from)
                .collect(Collectors.toList());
    }
}
