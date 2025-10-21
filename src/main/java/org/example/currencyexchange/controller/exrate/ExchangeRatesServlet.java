package org.example.currencyexchange.controller.exrate;

import org.example.currencyexchange.dto.request.ExchangeRateRequestDTO;
import org.example.currencyexchange.dto.response.ExchangeRateResponseDTO;
import org.example.currencyexchange.controller.BaseServlet;
import org.example.currencyexchange.mapper.request.ExchangeRateRequestMapper;
import org.example.currencyexchange.service.ExchangeRateService;
import org.example.currencyexchange.validation.ExchangeRateValidator;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static javax.servlet.http.HttpServletResponse.*;

@WebServlet(urlPatterns = {"/exchangeRates"})
public class ExchangeRatesServlet extends BaseServlet {

    private final ExchangeRateService exchangeRateService = new ExchangeRateService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        handleRequest(resp, () -> {
            List<ExchangeRateResponseDTO> exchangeRates = exchangeRateService.getExchangeRates();
            sendJsonResponse(resp, SC_OK, exchangeRates);
        });
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        handleRequest(resp, () -> {
            ExchangeRateRequestDTO dto = ExchangeRateRequestMapper.from(req);
            ExchangeRateValidator.validate(dto);
            ExchangeRateResponseDTO exchangeRate = exchangeRateService.addExchangeRate(dto);
            sendJsonResponse(resp, SC_CREATED, exchangeRate);
        });
    }
}
