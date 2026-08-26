package com.jobtrack.jobtrack.controller;

import com.jobtrack.jobtrack.dto.RegisterRequest;
import com.jobtrack.jobtrack.dto.UserResponse;
import com.jobtrack.jobtrack.service.UserAccountService;
import com.jobtrack.jobtrack.dto.LoginRequest;
import com.jobtrack.jobtrack.dto.TokenResponse;
import com.jobtrack.jobtrack.service.AuthenticationService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserAccountService userAccountService;
    private final AuthenticationService authenticationService;

    public AuthController(
            UserAccountService userAccountService,
            AuthenticationService authenticationService) {
        this.userAccountService = userAccountService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @Valid @RequestBody RegisterRequest request) {
        UserResponse result = this.userAccountService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(result);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(
            @Valid @RequestBody LoginRequest request) {
        TokenResponse response = this.authenticationService.login(request);

        return ResponseEntity.ok(response);
    }
}