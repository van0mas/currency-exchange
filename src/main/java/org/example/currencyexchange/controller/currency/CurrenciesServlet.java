package org.example.currencyexchange.controller.currency;

import org.example.currencyexchange.dto.request.CurrencyRequestDTO;
import org.example.currencyexchange.dto.response.CurrencyResponseDTO;
import org.example.currencyexchange.controller.BaseServlet;
import org.example.currencyexchange.mapper.request.CurrencyRequestMapper;
import org.example.currencyexchange.service.CurrencyService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.example.currencyexchange.validation.CurrencyValidator;

import static javax.servlet.http.HttpServletResponse.*;

@WebServlet(urlPatterns = {"/currencies"})
public class CurrenciesServlet extends BaseServlet {

    private final CurrencyService currencyService = new CurrencyService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        handleRequest(resp, () -> {
            List<CurrencyResponseDTO> currencies = currencyService.getCurrencies();
            sendJsonResponse(resp, SC_OK, currencies);
        });
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        handleRequest(resp, () -> {
            CurrencyRequestDTO dto = CurrencyRequestMapper.from(req);
            CurrencyValidator.validate(dto);
            CurrencyResponseDTO currency = currencyService.addCurrency(dto);
            sendJsonResponse(resp, SC_CREATED, currency);
        });
    }
}
