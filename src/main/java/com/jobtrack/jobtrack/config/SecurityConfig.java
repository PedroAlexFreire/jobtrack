package com.jobtrack.jobtrack.config;

import com.jobtrack.jobtrack.model.UserAccount;
import com.jobtrack.jobtrack.repository.UserAccountRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Locale;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/api/auth/register",
                                "/api/health"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(
            UserAccountRepository repository
    ) {
        return username -> {
            String normalizedEmail = username
                    .trim()
                    .toLowerCase(Locale.ROOT);

            UserAccount account = repository
                    .findByEmail(normalizedEmail)
                    .orElseThrow(() ->
                            new UsernameNotFoundException(
                                    "Invalid credentials"
                            )
                    );

            UserDetails user = User
                    .withUsername(account.getEmail())
                    .password(account.getPasswordHash())
                    .roles("USER")
                    .build();

            return user;
        };
    }
}