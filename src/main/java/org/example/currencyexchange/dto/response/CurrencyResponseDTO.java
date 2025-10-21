package org.example.currencyexchange.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CurrencyResponseDTO {
    private int id;
    private String name;
    private String code;
    private String sign;
}
