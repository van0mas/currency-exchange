package org.example.currencyexchange.mapper.model;

import org.example.currencyexchange.dto.request.CurrencyRequestDTO;
import org.example.currencyexchange.model.CurrencyModel;

public class CurrencyDTOMapper {

    public static CurrencyModel from(CurrencyRequestDTO dto) {
        return new CurrencyModel(
                dto.getName(),
                dto.getCode(),
                dto.getSign()
        );
    }
}
