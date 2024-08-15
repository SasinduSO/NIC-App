package com.example.dashboard_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("http://localhost:3000")  // Allow requests from this origin
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // Allow specific HTTP methods
                        .allowedHeaders("*")  // Allow all headers
                        .allowCredentials(true);  // Allow credentials (cookies, authorization headers)
            }
        };
    }
}
