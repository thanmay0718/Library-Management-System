
package com.library.management.config;

import com.library.management.security.JwtAuthenticationFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth

                        // Public authentication and registration endpoints
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/users/register"
                        ).permitAll()

                        // Read books: USER and ADMIN
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/book/**",
                                "/api/books/**"
                        ).hasAnyRole("USER", "ADMIN")

                        // Create books: ADMIN only
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/book/**",
                                "/api/books/**"
                        ).hasRole("ADMIN")

                        // Update books: ADMIN only
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/book/**",
                                "/api/books/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/api/book/**",
                                "/api/books/**"
                        ).hasRole("ADMIN")

                        // Delete books: ADMIN only
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/book/**",
                                "/api/books/**"
                        ).hasRole("ADMIN")

                        // Loan Management endpoints
                        // Borrow, return, renew, and view personal loans
                        .requestMatchers("/api/loans/**").authenticated()

                        // All remaining endpoints require authentication
                        .anyRequest().authenticated()
                )

                // JWT authentication: no HTTP session
                .sessionManagement(session -> session
                        .sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                // Execute the JWT filter before username/password auth
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}
