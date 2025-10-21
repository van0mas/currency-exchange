package org.example.currencyexchange.controller.exrate;

import org.example.currencyexchange.dto.request.ExchangeRateRequestDTO;
import org.example.currencyexchange.dto.response.ExchangeRateResponseDTO;
import org.example.currencyexchange.controller.BaseServlet;
import org.example.currencyexchange.mapper.request.ExchangeRateRequestMapper;
import org.example.currencyexchange.service.ExchangeRateService;
import org.example.currencyexchange.validation.ExchangeRateValidator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static javax.servlet.http.HttpServletResponse.*;

@WebServlet(urlPatterns = {"/exchangeRate/*"})
public class ExchangeRateServlet extends BaseServlet {

    private final ExchangeRateService exchangeRateService = new ExchangeRateService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();

        handleRequest(resp, () -> {
            ExchangeRateRequestDTO dto = ExchangeRateRequestMapper.from(pathInfo);
            ExchangeRateResponseDTO exchangeRate = exchangeRateService.getExchangeRateByCode(dto);
            sendJsonResponse(resp, SC_OK, exchangeRate);
        });
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        // Parse x-www-form-urlencoded manually since getParameter does not work with PATCH
        String body = req.getReader().lines().collect(Collectors.joining());
        Map<String, String> params = parseFormData(body);

        String pathInfo = req.getPathInfo();
        String rate = params.get("rate");

        handleRequest(resp, () -> {
            ExchangeRateRequestDTO dto = ExchangeRateRequestMapper.from(pathInfo, rate);
            ExchangeRateValidator.validate(dto);
            ExchangeRateResponseDTO exchangeRate = exchangeRateService.updateExchangeRate(dto);
            sendJsonResponse(resp, SC_CREATED, exchangeRate);
        });
    }

    // Override the service method to support PATCH requests
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if ("PATCH".equalsIgnoreCase(req.getMethod())) {
            doPatch(req, resp);
        } else {
            super.service(req, resp); // Handle GET, POST, PUT, DELETE, etc.
        }
    }

    // Helper method to parse x-www-form-urlencoded data from the request body
    private Map<String, String> parseFormData(String body) throws UnsupportedEncodingException {
        Map<String, String> result = new HashMap<>();
        String[] pairs = body.split("&");

        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                String key = URLDecoder.decode(keyValue[0], "UTF-8");
                String value = URLDecoder.decode(keyValue[1], "UTF-8");
                result.put(key, value);
            }
        }

        return result;
    }
}

