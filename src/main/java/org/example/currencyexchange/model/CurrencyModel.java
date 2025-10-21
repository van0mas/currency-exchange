package org.example.currencyexchange.model;

import lombok.Data;
import lombok.Getter;

import java.util.Objects;

@Getter
public class CurrencyModel {
    private int id;
    private final String name;
    private final String code;
    private final String sign;

    public CurrencyModel(String fullName, String code, String sign) {
        this.name = fullName;
        this.code = code;
        this.sign = sign;
    }

    public CurrencyModel(int id, String fullName, String code, String sign) {
        this.id = id;
        this.name = fullName;
        this.code = code;
        this.sign = sign;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CurrencyModel)) return false;
        return Objects.equals(name, ((CurrencyModel) o).name) &&
               Objects.equals(code, ((CurrencyModel) o).code) &&
               Objects.equals(sign, ((CurrencyModel) o).sign);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, code, sign);
    }
}
