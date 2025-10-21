package org.example.currencyexchange.controller;

import com.google.gson.Gson;
import org.example.currencyexchange.exceptions.AppException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public abstract class BaseServlet extends HttpServlet {

    protected final Gson gson = new Gson();

    protected void handleRequest(HttpServletResponse resp, ThrowingAction action) throws IOException {
        try {
            action.execute();
        } catch (AppException e) {
            sendError(resp, e.getStatusCode(), e.getMessage());
        } catch (Exception e) {
            sendError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error");
        }
    }

    protected void sendJsonResponse(HttpServletResponse resp, int status, Object data) throws IOException {
        resp.setStatus(status);
        resp.setContentType("application/json; charset=UTF-8");
        resp.getWriter().write(gson.toJson(data));
    }

    protected void sendError(HttpServletResponse resp, int status, String message) throws IOException {
        sendJsonResponse(resp, status, Map.of("message", message));
    }

    @FunctionalInterface
    protected interface ThrowingAction {
        void execute() throws Exception;
    }
}
