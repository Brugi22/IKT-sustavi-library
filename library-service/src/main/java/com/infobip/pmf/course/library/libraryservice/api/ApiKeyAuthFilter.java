package com.infobip.pmf.course.library.libraryservice.api;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ApiKeyAuthFilter extends HttpFilter {

    private static final List<String> VALID_API_KEYS = List.of(
            "la9psd71atbpgeg7fvvx",
            "ox9w79g2jwctzww2hcyb",
            "othyqhps18srg7fdj0p9"
    );

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String apiKey = request.getHeader("Authorization");
        if (apiKey == null || !apiKey.startsWith("App ") || !VALID_API_KEYS.contains(apiKey.substring(4))) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: Invalid API key.");
            return;
        }
        chain.doFilter(request, response);
    }
}


