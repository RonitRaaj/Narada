package com.narada.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        // Allow cookies and authentication headers across origins
        config.setAllowCredentials(true);
        
        // Explicitly white-list the frontend development origins
        config.setAllowedOrigins(Arrays.asList(
            "http://localhost:5173", 
            "http://127.0.0.1:5173", 
            "http://localhost:8080"
        ));
        
        // CRITICAL: Explicitly allow your custom token header to enter the server!
        config.setAllowedHeaders(Arrays.asList(
            "Origin", "Content-Type", "Accept", "X-Session-Token", "Authorization"
        ));
        
        // CRITICAL: Explicitly let the frontend see the response token header
        config.setExposedHeaders(Arrays.asList("X-Session-Token"));
        
        // Allow all standard request actions
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}