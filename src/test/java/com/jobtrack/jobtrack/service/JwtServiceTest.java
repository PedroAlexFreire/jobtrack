package com.jobtrack.jobtrack.service;

import com.jobtrack.jobtrack.dto.TokenResponse;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JwtServiceTest {

    @Test
    void generateTokenCreatesExpectedClaimsAndResponse() {
        JwtEncoder jwtEncoder = mock(JwtEncoder.class);

        JwtService service = new JwtService(
                jwtEncoder,
                3600
        );

        Jwt encodedJwt = Jwt
                .withTokenValue("signed-jwt")
                .header("alg", "HS256")
                .claim(
                        "sub",
                        "ana.ownership@example.com"
                )
                .build();

        when(jwtEncoder.encode(
                any(JwtEncoderParameters.class)
        )).thenReturn(encodedJwt);

        TokenResponse result = service.generateToken(
                "ana.ownership@example.com"
        );

        assertEquals(
                "signed-jwt",
                result.getAccessToken()
        );

        assertEquals(
                "Bearer",
                result.getTokenType()
        );

        assertEquals(
                3600,
                result.getExpiresIn()
        );

        ArgumentCaptor<JwtEncoderParameters> captor =
                ArgumentCaptor.forClass(
                        JwtEncoderParameters.class
                );

        verify(jwtEncoder).encode(captor.capture());

        JwtClaimsSet claims =
                captor.getValue().getClaims();

        assertEquals(
                "jobtrack",
                claims.getClaimAsString("iss")
        );

        assertEquals(
                "ana.ownership@example.com",
                claims.getSubject()
        );

        assertNotNull(claims.getIssuedAt());
        assertNotNull(claims.getExpiresAt());

        long lifetimeSeconds = Duration.between(
                claims.getIssuedAt(),
                claims.getExpiresAt()
        ).getSeconds();

        assertEquals(3600, lifetimeSeconds);
    }
}