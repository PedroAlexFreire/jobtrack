package com.jobtrack.jobtrack.service;

import com.jobtrack.jobtrack.dto.LoginRequest;
import com.jobtrack.jobtrack.dto.TokenResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthenticationService(
            AuthenticationManager authenticationManager,
            JwtService jwtService
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public TokenResponse login(LoginRequest request) {
        String normalizedEmail = request
                .getEmail()
                .trim()
                .toLowerCase(Locale.ROOT);

        Authentication authentication =
                this.authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                normalizedEmail,
                                request.getPassword()
                        )
                );

        return this.jwtService.generateToken(
                authentication.getName()
        );
    }
}