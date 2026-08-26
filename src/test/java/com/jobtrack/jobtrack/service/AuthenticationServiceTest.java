package com.jobtrack.jobtrack.service;

import com.jobtrack.jobtrack.dto.LoginRequest;
import com.jobtrack.jobtrack.dto.TokenResponse;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthenticationServiceTest {

    @Test
    void loginAuthenticatesNormalizedEmailAndReturnsToken() {
        AuthenticationManager authenticationManager =
                mock(AuthenticationManager.class);

        JwtService jwtService =
                mock(JwtService.class);

        AuthenticationService service =
                new AuthenticationService(
                        authenticationManager,
                        jwtService
                );

        LoginRequest request = new LoginRequest(
                "  Ana.Ownership@Example.com  ",
                "SecurePass123!"
        );

        Authentication authenticatedUser =
                mock(Authentication.class);

        when(authenticationManager.authenticate(
                any(Authentication.class)
        )).thenReturn(authenticatedUser);

        when(authenticatedUser.getName())
                .thenReturn("ana.ownership@example.com");

        TokenResponse expectedResponse =
                new TokenResponse(
                        "signed-jwt",
                        "Bearer",
                        3600
                );

        when(jwtService.generateToken(
                "ana.ownership@example.com"
        )).thenReturn(expectedResponse);

        TokenResponse result = service.login(request);

        assertSame(expectedResponse, result);

        ArgumentCaptor<Authentication> captor =
                ArgumentCaptor.forClass(
                        Authentication.class
                );

        verify(authenticationManager)
                .authenticate(captor.capture());

        Authentication attemptedAuthentication =
                captor.getValue();

        assertEquals(
                "ana.ownership@example.com",
                attemptedAuthentication.getName()
        );

        assertEquals(
                "SecurePass123!",
                attemptedAuthentication.getCredentials()
        );

        verify(jwtService).generateToken(
                "ana.ownership@example.com"
        );
    }
}