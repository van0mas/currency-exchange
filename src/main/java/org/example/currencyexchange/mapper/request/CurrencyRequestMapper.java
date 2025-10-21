package org.example.currencyexchange.mapper.request;

import org.example.currencyexchange.dto.request.CurrencyRequestDTO;

import javax.servlet.http.HttpServletRequest;

public class CurrencyRequestMapper{

    public static CurrencyRequestDTO from(HttpServletRequest req) {
        String fullName = req.getParameter("name");
        String code = req.getParameter("code");
        String sign = req.getParameter("sign");

        return new CurrencyRequestDTO(fullName, code, sign);
    }
}
