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
                //CSRF (Cross-Site Request Forgery) est une protection contre les attaques
                // où un site malveillant pourrait soumettre des requêtes à votre application
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/**")  // Désactive CSRF pour /api/**
                )
                //Les URLs /api/** sont accessibles à tous, même sans authentification
                //Les URLs /admin/** nécessitent le rôle "ADMIN"
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**").permitAll()  // Autorise /api/** sans authentification
                        .requestMatchers("/admin/**").hasRole("ADMIN")  // Réservé aux admins
                        .anyRequest().authenticated()  // Tout le reste nécessite une authentification
                )
                //Utilise /login comme page de connexion (accessible à tous)
                //Redirige vers /home après une connexion réussie
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                        .defaultSuccessUrl("/home")
                )
                //Utilise /logout comme URL de déconnexion
                //Redirige vers /login?logout après déconnexion
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                )
                //Crée une session seulement si nécessaire
                //Limite à une seule session active par utilisateur (empêche les connexions multiples)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .maximumSessions(1)
                )
                //Empêche l'application d'être chargée dans un iframe (protection contre le clickjacking)
                //Active la protection XSS (Cross-Site Scripting) du navigateur
                .headers(headers -> headers
                        .frameOptions(frame -> frame.deny())  // Empêche le chargement dans une iframe
                        .xssProtection(withDefaults())  // Active la protection XSS avec les paramètres par défaut
                        .cacheControl(withDefaults())  // Désactive le cache
                )
                //Permet à votre API d'être appelée depuis http://localhost:4200 (souvent une application Angular)
                //Autorise les méthodes GET, POST, PUT et DELETE
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
                //Si un utilisateur non authentifié tente d'accéder à une ressource protégée, renvoie une erreur 401 (Unauthorized)
                //Si un utilisateur authentifié mais sans les droits nécessaires tente d'accéder à une ressource, renvoie une erreur 403 (Forbidden)
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((req, res, ex) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"))
                        .accessDeniedHandler((req, res, ex) -> res.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden"))
                );

        return http.build();
    }
}