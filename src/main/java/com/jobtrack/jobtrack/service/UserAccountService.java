package com.jobtrack.jobtrack.service;

import com.jobtrack.jobtrack.dto.RegisterRequest;
import com.jobtrack.jobtrack.dto.UserResponse;
import com.jobtrack.jobtrack.model.UserAccount;
import com.jobtrack.jobtrack.repository.UserAccountRepository;
import com.jobtrack.jobtrack.exception.EmailAlreadyExistsException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Locale;

@Service
public class UserAccountService {

    private final UserAccountRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserAccountService(
            UserAccountRepository repository,
            PasswordEncoder passwordEncoder
    ) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse register(RegisterRequest request) {
        String normalizedEmail = request
                .getEmail()
                .trim()
                .toLowerCase(Locale.ROOT);

        if (this.repository.existsByEmail(normalizedEmail)) {
            throw new EmailAlreadyExistsException();
        }

        String passwordHash =
                this.passwordEncoder.encode(request.getPassword());

        UserAccount user = new UserAccount(
                null,
                request.getName().trim(),
                normalizedEmail,
                passwordHash
        );

        UserAccount savedUser = this.repository.save(user);

        return new UserResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail()
        );
    }
}