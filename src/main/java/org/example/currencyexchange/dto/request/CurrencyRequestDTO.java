package org.example.currencyexchange.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CurrencyRequestDTO {
    private String name;
    private String code;
    private String sign;
}
