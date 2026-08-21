package com.jobtrack.jobtrack.service;

import com.jobtrack.jobtrack.dto.RegisterRequest;
import com.jobtrack.jobtrack.dto.UserResponse;
import com.jobtrack.jobtrack.model.UserAccount;
import com.jobtrack.jobtrack.repository.UserAccountRepository;
import com.jobtrack.jobtrack.exception.EmailAlreadyExistsException;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserAccountServiceTest {

        @Test
        void registerCreatesUserWithEncodedPassword() {
                UserAccountRepository repository = mock(UserAccountRepository.class);

                PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

                UserAccountService service = new UserAccountService(repository, passwordEncoder);

                RegisterRequest request = new RegisterRequest(
                                "Pedro",
                                "Pedro@Example.com",
                                "password123");

                when(repository.existsByEmail("pedro@example.com"))
                                .thenReturn(false);

                when(passwordEncoder.encode("password123"))
                                .thenReturn("{bcrypt}encoded-password");

                UserAccount savedUser = new UserAccount(
                                1L,
                                "Pedro",
                                "pedro@example.com",
                                "{bcrypt}encoded-password");

                when(repository.save(any(UserAccount.class)))
                                .thenReturn(savedUser);

                UserResponse result = service.register(request);

                assertNotNull(result);
                assertEquals(1L, result.getId());
                assertEquals("Pedro", result.getName());
                assertEquals("pedro@example.com", result.getEmail());

                verify(passwordEncoder).encode("password123");
        }

        @Test
        void registerThrowsExceptionWhenEmailAlreadyExists() {
                UserAccountRepository repository = mock(UserAccountRepository.class);

                PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

                UserAccountService service = new UserAccountService(
                                repository,
                                passwordEncoder);

                RegisterRequest request = new RegisterRequest(
                                "Pedro",
                                "pedro@example.com",
                                "password123");

                when(repository.existsByEmail("pedro@example.com"))
                                .thenReturn(true);

                EmailAlreadyExistsException exception = assertThrows(
                                EmailAlreadyExistsException.class,
                                () -> service.register(request));

                assertEquals(
                                "An account with this email already exists",
                                exception.getMessage());

                verifyNoInteractions(passwordEncoder);

                verify(repository, never())
                                .save(any(UserAccount.class));
        }
}