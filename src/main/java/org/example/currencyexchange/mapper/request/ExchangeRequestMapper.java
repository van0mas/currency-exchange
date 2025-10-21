package org.example.currencyexchange.mapper.request;

import org.example.currencyexchange.dto.request.ExchangeRequestDTO;

import javax.servlet.http.HttpServletRequest;

public class ExchangeRequestMapper {

    public static ExchangeRequestDTO from(HttpServletRequest req) {
        String base = req.getParameter("from");
        String target = req.getParameter("to");
        String amount = req.getParameter("amount");

        return new ExchangeRequestDTO(base, target, amount);
    }
}
