package com.jobtrack.jobtrack.controller;

import com.jobtrack.jobtrack.repository.UserAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @BeforeEach
    void cleanDatabase() {
        this.userAccountRepository.deleteAll();
    }

    @Test
    void registerCreatesUserAndReturnsCreated() throws Exception {
        String requestBody = """
                {
                    "name": "Pedro",
                    "email": "pedro@example.com",
                    "password": "password123"
                }
                """;

        this.mockMvc
                .perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Pedro"))
                .andExpect(jsonPath("$.email").value("pedro@example.com"));

        assertTrue(this.userAccountRepository.existsByEmail("pedro@example.com"));
    }

    @Test
    void registerReturnsConflictWhenEmailAlreadyExists() throws Exception {
        this.registerUser("Pedro", "pedro@example.com", "password123");

        String requestBody = """
                {
                    "name": "Pedro",
                    "email": "pedro@example.com",
                    "password": "password123"
                }
                """;

        this.mockMvc
                .perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("An account with this email already exists"));
    }

    @Test
    void loginReturnsTokenWhenCredentialsAreValid() throws Exception {
        this.registerUser("Pedro", "pedro@example.com", "password123");

        String loginRequestBody = """
                {
                    "email": "pedro@example.com",
                    "password": "password123"
                }
                """;

        this.mockMvc
                .perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.expiresIn").value(3600))
                .andExpect(jsonPath("$.accessToken").isNotEmpty());
    }

    @Test
    void loginReturnsUnauthorizedWhenPasswordIsInvalid() throws Exception {
        this.registerUser("Pedro", "pedro@example.com", "password123");

        String loginRequestBody = """
                {
                    "email": "pedro@example.com",
                    "password": "wrong-password"
                }
                """;

        this.mockMvc
                .perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestBody))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void registerReturnsBadRequestWhenPasswordIsTooShort() throws Exception {
        String requestBody = """
                {
                    "name": "Pedro",
                    "email": "pedro@example.com",
                    "password": "short"
                }
                """;

        this.mockMvc
                .perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors.password").value("Password must have at least 8 characters"));
    }

    @Test
    void registerReturnsBadRequestWhenEmailIsInvalid() throws Exception {
        String requestBody = """
                {
                    "name": "Pedro",
                    "email": "not-an-email",
                    "password": "password123"
                }
                """;

        this.mockMvc
                .perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors.email").value("Email must be valid"));
    }

    @Test
    void loginReturnsBadRequestWhenEmailIsInvalid() throws Exception {
        String requestBody = """
                {
                    "email": "not-an-email",
                    "password": "password123"
                }
                """;

        this.mockMvc
                .perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors.email").value("Email must be valid"));
    }

    @Test
    void loginReturnsBadRequestWhenPasswordIsBlank() throws Exception {
        String requestBody = """
                {
                    "email": "pedro@example.com",
                    "password": ""
                }
                """;

        this.mockMvc
                .perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors.password").value("Password is required"));
    }

    @Test
    void registerReturnsBadRequestWhenNameIsBlank() throws Exception {
        String requestBody = """
                {
                    "name": "",
                    "email": "pedro@example.com",
                    "password": "password123"
                }
                """;

        this.mockMvc
                .perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors.name").value("Name is required"));
    }

    private void registerUser(
            String name,
            String email,
            String password) throws Exception {
        String requestBody = """
                {
                    "name": "%s",
                    "email": "%s",
                    "password": "%s"
                }
                """.formatted(name, email, password);

        this.mockMvc
                .perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());
    }
}