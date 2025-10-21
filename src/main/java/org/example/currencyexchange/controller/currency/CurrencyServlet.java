package org.example.currencyexchange.controller.currency;

import org.example.currencyexchange.dto.response.CurrencyResponseDTO;
import org.example.currencyexchange.controller.BaseServlet;
import org.example.currencyexchange.service.CurrencyService;
import org.example.currencyexchange.validation.ValidationUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.*;

@WebServlet("/currency/*")
public class CurrencyServlet extends BaseServlet {

    private final CurrencyService currencyService = new CurrencyService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();

        if (!ValidationUtils.isValidCurrencyPath(pathInfo)) {
            sendError(resp, SC_BAD_REQUEST, "Currency code is missing");
            return;
        }

        String code = pathInfo.substring(1);

        if (!ValidationUtils.isValidCurrencyCode(code)) {
            sendError(resp, SC_BAD_REQUEST, "Invalid currency code");
            return;
        }

        handleRequest(resp, () -> {
            CurrencyResponseDTO currency = currencyService.getCurrencyByCode(code);
            sendJsonResponse(resp, SC_OK, currency);
        });
    }
}
