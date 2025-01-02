package com.mate.band.global.security.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;
import java.util.List;

/**
 * CORS 관리
 * @author : sungmin
 * @fileName : CustomCorsConfigurationSource
 * @since : 2024/12/17
 */
@Component
public class CustomCorsConfigurationSource implements CorsConfigurationSource {
    private final String ALLOWED_ORIGIN;
    private final List<String> ALLOWED_METHODS = List.of("GET", "POST", "PUT", "OPTIONS");

    public CustomCorsConfigurationSource(@Value("${url.client}") String BASE_URL) {
        this.ALLOWED_ORIGIN = BASE_URL;
    }

    @Override
    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Collections.singletonList(ALLOWED_ORIGIN));
        config.setAllowedHeaders(Collections.singletonList("*"));
        config.setAllowedMethods(ALLOWED_METHODS);
        config.setMaxAge(3600L);
        return config;
    }

}
