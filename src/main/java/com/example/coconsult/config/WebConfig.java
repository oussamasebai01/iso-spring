package com.example.coconsult.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
   /* @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // Autoriser tous les endpoints sous /api
                .allowedOrigins("http://localhost:4200") // Origine de votre Angular
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Méthodes HTTP autorisées
                .allowedHeaders("*") // Tous les en-têtes
                .allowCredentials(true); // Autoriser les cookies/credentials si nécessaire
    }*/
}