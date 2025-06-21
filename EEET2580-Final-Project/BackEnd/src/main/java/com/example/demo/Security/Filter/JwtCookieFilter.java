package com.example.demo.Security.Filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtCookieFilter extends OncePerRequestFilter {

    private static final String COOKIE_NAME = "jwtToken";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Retrieve the JWT token from cookies
        String token = getJwtTokenFromCookies(request);
        if (token != null) {
            // Set the Authorization header
            request = new JwtTokenRequestWrapper(request, token);
        }

        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }

    private String getJwtTokenFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (COOKIE_NAME.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}

