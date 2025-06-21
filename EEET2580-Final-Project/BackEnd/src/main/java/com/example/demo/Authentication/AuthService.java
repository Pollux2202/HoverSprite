package com.example.demo.Authentication;

import com.example.demo.User.enumType.UserType;
import com.example.demo.Security.Config.SecurityConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthService {

//    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long EXPIRATION_TIME = 360_000_000; // 1 day in milliseconds
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    public static String generateToken(String email, UserType roles) {
        return Jwts.builder()
                .setSubject(email)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SecurityConfig.key)
                .compact();
    }

    public static Claims introspectToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SecurityConfig.key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            logger.info("Token claims: {}", claims);
            return claims;
        } catch (SecurityException e) {
            logger.error("Invalid token signature: {}", e.getMessage());
            throw new SecurityException("Invalid token signature", e);
        } catch (Exception e) {
            logger.error("Token introspection failed: {}", e.getMessage());
            throw new RuntimeException("Token introspection failed", e);
        }
    }


    public static boolean isTokenExpired(String token) {
        if (token == null) {
            return true;
        }

        try {
            Date expirationDate = introspectToken(token).getExpiration();
            return expirationDate.before(new Date());
        } catch (Exception e) {
            logger.error("Token expiration check failed: {}", e.getMessage());
            return true;
        }
    }

    public static boolean isTokenValid(String token) {
        if (token == null) {
            return false;
        }

        try {
            Claims claims = introspectToken(token); // Validate signature and parse claims
            return !isTokenExpired(token); // Check if token is expired
        } catch (SecurityException e) {
            logger.error("Token validation failed due to security issues: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            logger.error("Token validation failed: {}", e.getMessage());
            return false;
        }
    }
}