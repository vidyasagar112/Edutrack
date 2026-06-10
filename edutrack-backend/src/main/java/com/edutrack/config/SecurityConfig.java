package com.edutrack.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.edutrack.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configure(http)) // ADD THIS LINE
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
            	    .requestMatchers("/api/auth/**").permitAll()
            	    .requestMatchers("/api/courses").permitAll()
            	    .requestMatchers("/api/courses/{id}").permitAll()
            	    .requestMatchers("/api/courses/search").permitAll()
            	    .requestMatchers("/api/courses/categories").permitAll()
            	    .requestMatchers("/api/courses/subjects").permitAll()
            	    .requestMatchers("/api/courses/search-filter").permitAll()
            	    .requestMatchers("/api/sections/course/**").permitAll()
            	    .requestMatchers("/api/ratings/course/**").permitAll()
            	    .requestMatchers("/api/quizzes/course/**").permitAll()
            	    .requestMatchers("/api/documents/course/**").permitAll()
            	    .requestMatchers("/api/documents/download/**").permitAll() // ← ADD THIS
            	    .requestMatchers("/swagger-ui/**").permitAll()
            	    .requestMatchers("/v3/api-docs/**").permitAll()
            	    .anyRequest().authenticated()
            	)
            .addFilterBefore(jwtAuthFilter,
                    UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}