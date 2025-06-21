package com.example.demo.Security.Filter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtDecoder jwtDecoder;

    public JwtAuthenticationFilter(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = extractToken(request);

        if (token != null) {
            try {
                Jwt jwt = jwtDecoder.decode(token);

                String email = jwt.getClaimAsString("sub");
                String rolesClaim = jwt.getClaimAsString("roles");

                Collection<SimpleGrantedAuthority> authorities = rolesClaim != null ?
                        Arrays.stream(rolesClaim.split(","))
                                .map(String::trim)
                                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                                .collect(Collectors.toList())
                        : Collections.emptyList();

                Authentication authentication = new JwtAuthenticationToken(email, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                e.printStackTrace();  // Log or handle the exception as needed
            }
        }

        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        String token = null;
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }
        return token;
    }

    public String extractEmailFromToken(HttpServletRequest request) {
        String token = extractToken(request);
        if (token != null) {
            try {
                Jwt jwt = jwtDecoder.decode(token);
                return jwt.getClaimAsString("sub"); // Adjust based on your claim
            } catch (Exception e) {
                e.printStackTrace(); // Log or handle the exception as needed
            }
        }
        return null;
    }

    public Collection<SimpleGrantedAuthority> extractRolesFromToken(HttpServletRequest request) {
        String token = extractToken(request);
        if (token != null) {
            try {
                Jwt jwt = jwtDecoder.decode(token);
                String rolesClaim = jwt.getClaimAsString("roles");
                if (rolesClaim != null) {
                    return Arrays.stream(rolesClaim.split(","))
                            .map(String::trim)
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                            .collect(Collectors.toList());
                }
            } catch (Exception e) {
                e.printStackTrace(); // Log or handle the exception as needed
            }
        }
        return Collections.emptyList(); // Return an empty list if no roles are found
    }

}