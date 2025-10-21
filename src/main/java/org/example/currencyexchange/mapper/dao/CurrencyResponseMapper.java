package org.example.currencyexchange.mapper.dao;

import org.example.currencyexchange.dto.response.CurrencyResponseDTO;
import org.example.currencyexchange.model.CurrencyModel;

import java.util.List;
import java.util.stream.Collectors;

public class CurrencyResponseMapper {
    public static CurrencyResponseDTO from(CurrencyModel model) {
        return new CurrencyResponseDTO(
                model.getId(),
                model.getName(),
                model.getCode(),
                model.getSign()
        );
    }

    public static List<CurrencyResponseDTO> from(List<CurrencyModel> list) {
        return list.stream()
                .map(CurrencyResponseMapper::from)
                .collect(Collectors.toList());
    }
}
