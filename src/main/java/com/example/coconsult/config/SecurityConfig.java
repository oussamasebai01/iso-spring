package com.example.coconsult.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Désactiver CSRF pour les API
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/**")
                )
                // Autoriser les requêtes sans authentification pour /api/**
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**").permitAll()  // Autorise /api/** sans authentification
                        .requestMatchers("/admin/**").hasRole("ADMIN")  // Réservé aux admins
                        .anyRequest().authenticated()  // Tout le reste nécessite une authentification
                )
                // Configurer la page de connexion
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                        .defaultSuccessUrl("/home")
                )
                // Configurer la déconnexion
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                )
                // Gestion des sessions
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .maximumSessions(1)
                )
                // Headers de sécurité
                .headers(headers -> headers
                        .frameOptions(frame -> frame.deny())
                        .xssProtection(withDefaults())
                        .cacheControl(withDefaults())
                )
                // Configuration CORS
                .cors(cors -> cors
                        .configurationSource(request -> {
                            CorsConfiguration config = new CorsConfiguration();
                            config.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
                            config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
                            config.setAllowedHeaders(Arrays.asList("*"));
                            config.setAllowCredentials(true);
                            return config;
                        })
                )
                // Gestion des erreurs d'authentification et d'accès
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((req, res, ex) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"))
                        .accessDeniedHandler((req, res, ex) -> res.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden"))
                );

        return http.build();
    }
}