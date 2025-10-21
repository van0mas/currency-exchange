package org.example.currencyexchange.controller;

import org.example.currencyexchange.dto.request.ExchangeRequestDTO;
import org.example.currencyexchange.dto.response.ExchangeResponseDTO;
import org.example.currencyexchange.mapper.request.ExchangeRequestMapper;
import org.example.currencyexchange.service.ExchangeService;
import org.example.currencyexchange.validation.ExchangeRateValidator;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.*;

@WebServlet("/exchange")
public class ExchangeServlet extends BaseServlet {

    private final ExchangeService exchangeService = new ExchangeService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        handleRequest(resp, () -> {
            ExchangeRequestDTO reqDTO = ExchangeRequestMapper.from(req);
            ExchangeRateValidator.validate(reqDTO);
            ExchangeResponseDTO respDTO = exchangeService.exchange(reqDTO);
            sendJsonResponse(resp, SC_CREATED, respDTO);
        });
    }
}
