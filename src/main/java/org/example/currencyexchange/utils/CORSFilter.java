/*
package org.example.currencyexchange.utils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class CORSFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");

        // Allow requests from any frontend
        String origin = req.getHeader("Origin");
        if (origin != null) {
            resp.setHeader("Access-Control-Allow-Origin", origin);
        }

        resp.setHeader("Vary", "Origin");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Origin, Content-Type, Accept, Authorization");
        resp.setHeader("Access-Control-Allow-Credentials", "true");

        // If this is a preflight request, respond immediately
        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
            resp.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        // Continue the filter chain
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void destroy() {}
}
*/
