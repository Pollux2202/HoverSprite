package com.example.demo.Authentication;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private JwtDecoder jwtDecoder;
    @Autowired
    private AuthService authService;

    @GetMapping("/verifyToken")
    public ResponseEntity<Map<String, Boolean>> verifyToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String token = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwtToken".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        boolean isValid = false;
        boolean isExpired = true;

        if (token != null) {
            try {
                Jwt jwt = jwtDecoder.decode(token);
                isValid = true;
                isExpired = jwt.getExpiresAt().isBefore(new Date().toInstant());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Map<String, Boolean> response = new HashMap<>();
        response.put("isValid", isValid);
        response.put("isExpired", isExpired);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwtToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // Use if you are on HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(0); // Expire the cookie immediately
        response.addCookie(cookie);
        return ResponseEntity.ok("Logged out successfully");
    }





}

