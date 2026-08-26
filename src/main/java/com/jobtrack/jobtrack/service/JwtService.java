package com.jobtrack.jobtrack.service;

import com.jobtrack.jobtrack.dto.TokenResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class JwtService {

    private final JwtEncoder jwtEncoder;
    private final long expirationSeconds;

    public JwtService(
            JwtEncoder jwtEncoder,
            @Value("${security.jwt.expiration-seconds}") long expirationSeconds) {
        this.jwtEncoder = jwtEncoder;
        this.expirationSeconds = expirationSeconds;
    }

    public TokenResponse generateToken(String email) {
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plusSeconds(this.expirationSeconds);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("jobtrack")
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .subject(email)
                .build();

        String token = this.jwtEncoder
                .encode(JwtEncoderParameters.from(claims))
                .getTokenValue();

        return new TokenResponse(
                token,
                "Bearer",
                this.expirationSeconds);
    }
}