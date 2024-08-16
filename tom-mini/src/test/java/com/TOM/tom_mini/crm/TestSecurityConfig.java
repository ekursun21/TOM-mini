package com.TOM.tom_mini.crm;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class TestSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // Disable CSRF for testing
                .authorizeRequests(auth -> auth
                        .requestMatchers("/api/customer/public/**").permitAll()
                        .requestMatchers("/api/customer/private/**").hasAnyRole("USER")
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());  // Use HTTP Basic for simplicity in tests

        return http.build();
    }
}
