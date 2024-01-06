package com.example.application.config.cors;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class CorsConfig {

    private static final List<String> ALLOWED_ORIGINS = List.of("*");
    private static final List<String> ALLOWED_METHODS = List.of("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH");
    private static final List<String> ALLOWED_HEADERS = List.of(
            "Content-Language",
            "Origin",
            "X-Requested-With",
            "Content-Type",
            "Accept",
            "Authorization",
            "Access-Control-Allow-Headers",
            "Access-Control-Request-Method",
            "Access-Control-Allow-Methods",
            "Access-Control-Allow-Credentials",
            "Access-Control-Request-Headers",
            "Access-Control-Allow-Origin",
            "Accept-Encoding");

    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedMethods(ALLOWED_METHODS);
        configuration.setAllowedOriginPatterns(ALLOWED_ORIGINS);
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(600L);
        configuration.setAllowedHeaders(ALLOWED_HEADERS);
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
