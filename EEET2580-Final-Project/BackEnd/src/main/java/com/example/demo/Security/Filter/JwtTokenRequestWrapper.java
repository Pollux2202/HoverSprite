package com.example.demo.Security.Filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;


public class JwtTokenRequestWrapper extends HttpServletRequestWrapper {

    private final String token;

    public JwtTokenRequestWrapper(HttpServletRequest request, String token) {
        super(request);
        this.token = token;
    }

    @Override
    public String getHeader(String name) {
        if ("Authorization".equalsIgnoreCase(name)) {
            return "Bearer " + token;
        }
        return super.getHeader(name);
    }
}

