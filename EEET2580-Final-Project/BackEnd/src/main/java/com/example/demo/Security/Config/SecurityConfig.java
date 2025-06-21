package com.example.demo.Security.Config;

import javax.crypto.SecretKey;

import org.json.HTTP;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.Security.Filter.JwtAuthenticationFilter;
import com.example.demo.Security.Filter.JwtCookieFilter;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    public static SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    private final String[] PUBLIC_ENDPOINT = {
            "/farmer/login", "/farmer/register", "/farmer/email/{email}",
            "receptionist/email/{email}", "sprayer/email/{email}",
            "/sprayer/login", "/receptionist/login", "/auth/verifyToken", "/receptionist/create", "/spray-sessions/create"
            , "/sprayer/create",
            "spray-order/create", "/auth/logout", "/sprayer/paged", "/spray-sessions/getDate"
    };


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http

                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF for simplicity
                .authorizeHttpRequests(authorize -> authorize
                                .requestMatchers(PUBLIC_ENDPOINT).permitAll() // Public endpoints

                                .requestMatchers(HttpMethod.POST, "spray-order/create").hasRole("RECEPTIONIST")
                                .requestMatchers(HttpMethod.POST, "/spray-order/farmer_created").hasRole("FARMER")// Role-based access
                                .requestMatchers("/receptionist/**", "spray-order/cancel/{sprayId}").hasRole("RECEPTIONIST")
                                .requestMatchers(HttpMethod.GET, "/receptionist/spray-orders/all").hasRole("RECEPTIONIST")
                                .requestMatchers(HttpMethod.GET, "/api/notifications/farmer").hasRole("FARMER")
                                .requestMatchers(HttpMethod.GET, "/api/notifications/sprayer").hasRole("SPRAYER")
                                .requestMatchers(HttpMethod.GET, "/farmer/receptionist_contacts").hasRole("FARMER")
                                .requestMatchers(HttpMethod.POST, "spray-order/assign-sprayers").hasRole("RECEPTIONIST")
                                .requestMatchers(HttpMethod.GET, "/feedback/all").hasRole("RECEPTIONIST")
//                        .requestMatchers("/sprayer/**").hasRole("SPRAYER")
                                .requestMatchers(HttpMethod.POST, "/spray-order/sprayer-confirmed").hasRole("SPRAYER")
                                .requestMatchers(HttpMethod.PUT, "/spray-order/confirm/{sprayId}").hasRole("RECEPTIONIST")
                                .requestMatchers(HttpMethod.PUT, "/sprayer/confirm/{orderId}").hasRole("SPRAYER")
                                .requestMatchers(HttpMethod.PUT, "/farmer/confirm/{sprayId}").hasRole("FARMER")
                                .requestMatchers(HttpMethod.POST, "/feedback/submit").hasRole("FARMER")
                                .requestMatchers(HttpMethod.GET, "/sprayer/spray-orders").hasRole("SPRAYER")
                                .requestMatchers(HttpMethod.GET, "/farmer/spray-orders").hasRole("FARMER")
                                .requestMatchers(HttpMethod.GET, "/farmer/{fId}").hasRole("RECEPTIONIST")
                                .anyRequest().authenticated() // Secure all other endpoints
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwtConfigurer -> jwtConfigurer
                                .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Stateless sessions
                )
                .addFilterBefore(new JwtCookieFilter(), UsernamePasswordAuthenticationFilter.class);// Add JWT filter
//                .cors(cors -> cors.configurationSource(corsConfigurationSource()));
        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_"); // Remove default "ROLE_" prefix if needed
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }


    @Bean
    public JwtDecoder jwtDecoder() {
        // Create the JWT decoder using the secret key
        return NimbusJwtDecoder.withSecretKey(key).build();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtDecoder jwtDecoder) {
        return new JwtAuthenticationFilter(jwtDecoder);
    }
}
