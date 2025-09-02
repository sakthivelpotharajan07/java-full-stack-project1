package com.zidio.zidio_connect.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .cors(cors -> {})
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        // Define authorization rules here with precise paths
        http.authorizeHttpRequests(auth -> auth
            // --- PUBLIC ENDPOINTS ---
            .requestMatchers("/api/auth/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/jobs").permitAll()

            // --- RECRUITER-ONLY ENDPOINTS (Most Specific Rules First) ---
            .requestMatchers(HttpMethod.PUT, "/api/applications/{id}/status").hasAuthority("ROLE_RECRUITER")
            .requestMatchers("/api/applications/job/**").hasAuthority("ROLE_RECRUITER")
            .requestMatchers("/api/jobs/recruiter/**").hasAuthority("ROLE_RECRUITER")
            .requestMatchers(HttpMethod.POST, "/api/jobs").hasAuthority("ROLE_RECRUITER")
            .requestMatchers(HttpMethod.DELETE, "/api/jobs/**").hasAuthority("ROLE_RECRUITER")

            // --- STUDENT-ONLY ENDPOINTS ---
            .requestMatchers(HttpMethod.POST, "/api/applications").hasAuthority("ROLE_STUDENT")
            .requestMatchers("/api/applications/user/**").hasAuthority("ROLE_STUDENT")
            .requestMatchers("/api/profiles/**").hasAuthority("ROLE_STUDENT")

            // --- ADMIN-ONLY ENDPOINTS ---
            .requestMatchers("/api/admin/**").hasAuthority("ROLE_ADMIN")
            
            // --- DEFAULT RULE ---
            .anyRequest().authenticated()
        );

        return http.build();
    }
}