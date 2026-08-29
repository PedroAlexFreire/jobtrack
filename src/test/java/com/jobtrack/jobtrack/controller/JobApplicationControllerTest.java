package com.jobtrack.jobtrack.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.springframework.http.MediaType;
import com.jobtrack.jobtrack.repository.JobApplicationRepository;
import com.jobtrack.jobtrack.repository.UserAccountRepository;
import com.jobtrack.jobtrack.service.JwtService;
import com.jobtrack.jobtrack.model.ApplicationStatus;
import com.jobtrack.jobtrack.model.JobApplication;
import com.jobtrack.jobtrack.model.UserAccount;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@SpringBootTest
@AutoConfigureMockMvc
class JobApplicationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @BeforeEach
    void cleanDatabase() {
        this.jobApplicationRepository.deleteAll();
        this.userAccountRepository.deleteAll();
    }

    @Test
    void getAllApplicationsReturnsOnlyAuthenticatedUserApplications() throws Exception {
        UserAccount pedro = this.userAccountRepository.save(
                new UserAccount(
                        null,
                        "Pedro",
                        "pedro@example.com",
                        "{noop}password"));

        UserAccount ana = this.userAccountRepository.save(
                new UserAccount(
                        null,
                        "Ana",
                        "ana@example.com",
                        "{noop}password"));
        JobApplication pedroApplication = new JobApplication(
                null,
                "Microsoft",
                "Junior Java Developer",
                ApplicationStatus.APPLIED);
        pedroApplication.setOwner(pedro);
        this.jobApplicationRepository.save(pedroApplication);

        JobApplication anaApplication = new JobApplication(
                null,
                "Spotify",
                "Backend Developer",
                ApplicationStatus.INTERVIEW);
        anaApplication.setOwner(ana);
        this.jobApplicationRepository.save(anaApplication);

        String accessToken = this.jwtService
                .generateToken("pedro@example.com")
                .getAccessToken();

        this.mockMvc
                .perform(get("/api/applications")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].company").value("Microsoft"))
                .andExpect(jsonPath("$[0].position").value("Junior Java Developer"))
                .andExpect(jsonPath("$[0].status").value("APPLIED"));

    }

    @Test
    void getAllApplicationsReturnsUnauthorizedWithoutToken() throws Exception {
        this.mockMvc
                .perform(get("/api/applications"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getApplicationByIdReturnsNotFoundForAnotherUsersApplication() throws Exception {
        UserAccount pedro = this.userAccountRepository.save(
                new UserAccount(
                        null,
                        "Pedro",
                        "pedro@example.com",
                        "{noop}password"));

        UserAccount ana = this.userAccountRepository.save(
                new UserAccount(
                        null,
                        "Ana",
                        "ana@example.com",
                        "{noop}password"));

        JobApplication anaApplication = new JobApplication(
                null,
                "Spotify",
                "Backend Developer",
                ApplicationStatus.INTERVIEW);
        anaApplication.setOwner(ana);
        anaApplication = this.jobApplicationRepository.save(anaApplication);

        String accessToken = this.jwtService
                .generateToken("pedro@example.com")
                .getAccessToken();

        this.mockMvc
                .perform(get("/api/applications/{id}", anaApplication.getId())
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteApplicationReturnsNotFoundForAnotherUsersApplication() throws Exception {
        UserAccount pedro = this.userAccountRepository.save(
                new UserAccount(
                        null,
                        "Pedro",
                        "pedro@example.com",
                        "{noop}password"));

        UserAccount ana = this.userAccountRepository.save(
                new UserAccount(
                        null,
                        "Ana",
                        "ana@example.com",
                        "{noop}password"));

        JobApplication anaApplication = new JobApplication(
                null,
                "Spotify",
                "Backend Developer",
                ApplicationStatus.INTERVIEW);
        anaApplication.setOwner(ana);
        anaApplication = this.jobApplicationRepository.save(anaApplication);

        String accessToken = this.jwtService
                .generateToken("pedro@example.com")
                .getAccessToken();

        this.mockMvc
                .perform(delete("/api/applications/{id}", anaApplication.getId())
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateApplicationReturnsNotFoundForAnotherUsersApplication() throws Exception {
        UserAccount pedro = this.userAccountRepository.save(
                new UserAccount(
                        null,
                        "Pedro",
                        "pedro@example.com",
                        "{noop}password"));

        UserAccount ana = this.userAccountRepository.save(
                new UserAccount(
                        null,
                        "Ana",
                        "ana@example.com",
                        "{noop}password"));

        JobApplication anaApplication = new JobApplication(
                null,
                "Spotify",
                "Backend Developer",
                ApplicationStatus.INTERVIEW);
        anaApplication.setOwner(ana);
        anaApplication = this.jobApplicationRepository.save(anaApplication);

        String accessToken = this.jwtService
                .generateToken("pedro@example.com")
                .getAccessToken();

        String requestBody = """
                {
                    "company": "Changed",
                    "position": "Changed Role",
                    "status": "OFFER"
                }
                """;

        this.mockMvc
                .perform(put("/api/applications/{id}", anaApplication.getId())
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound());
    }

    @Test
    void addApplicationReturnsCreatedWhenApplicationIsCreated() throws Exception {
        this.userAccountRepository.save(
                new UserAccount(
                        null,
                        "Pedro",
                        "pedro@example.com",
                        "{noop}password"));

        String accessToken = this.jwtService
                .generateToken("pedro@example.com")
                .getAccessToken();

        String requestBody = """
                {
                    "company": "Microsoft",
                    "position": "Junior Java Developer",
                    "status": "APPLIED"
                }
                """;

        this.mockMvc
                .perform(post("/api/applications")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.company").value("Microsoft"))
                .andExpect(jsonPath("$.position").value("Junior Java Developer"))
                .andExpect(jsonPath("$.status").value("APPLIED"));
    }

    @Test
    void addApplicationReturnsBadRequestWhenCompanyIsBlank() throws Exception {
        this.userAccountRepository.save(
                new UserAccount(
                        null,
                        "Pedro",
                        "pedro@example.com",
                        "{noop}password"));

        String accessToken = this.jwtService
                .generateToken("pedro@example.com")
                .getAccessToken();

        String requestBody = """
                {
                    "company": "",
                    "position": "Junior Java Developer",
                    "status": "APPLIED"
                }
                """;

        this.mockMvc
                .perform(post("/api/applications")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors.company").value("Company name is required"));
    }

    @Test
    void addApplicationReturnsBadRequestWhenStatusIsInvalid() throws Exception {
        this.userAccountRepository.save(
                new UserAccount(
                        null,
                        "Pedro",
                        "pedro@example.com",
                        "{noop}password"));

        String accessToken = this.jwtService
                .generateToken("pedro@example.com")
                .getAccessToken();

        String requestBody = """
                {
                    "company": "Microsoft",
                    "position": "Junior Java Developer",
                    "status": "UNKNOWN"
                }
                """;

        this.mockMvc
                .perform(post("/api/applications")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Malformed or unreadable JSON request"));
    }

    @Test
    void deleteApplicationDeletesOwnedApplication() throws Exception {
        UserAccount pedro = this.userAccountRepository.save(
                new UserAccount(
                        null,
                        "Pedro",
                        "pedro@example.com",
                        "{noop}password"));

        JobApplication pedroApplication = new JobApplication(
                null,
                "Microsoft",
                "Junior Java Developer",
                ApplicationStatus.APPLIED);
        pedroApplication.setOwner(pedro);
        pedroApplication = this.jobApplicationRepository.save(pedroApplication);

        String accessToken = this.jwtService
                .generateToken("pedro@example.com")
                .getAccessToken();

        this.mockMvc
                .perform(delete("/api/applications/{id}", pedroApplication.getId())
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isNoContent());

        assertFalse(this.jobApplicationRepository.existsById(pedroApplication.getId()));
    }

    @Test
    void updateApplicationUpdatesOwnedApplication() throws Exception {
        UserAccount pedro = this.userAccountRepository.save(
                new UserAccount(
                        null,
                        "Pedro",
                        "pedro@example.com",
                        "{noop}password"));

        JobApplication pedroApplication = new JobApplication(
                null,
                "Microsoft",
                "Junior Java Developer",
                ApplicationStatus.APPLIED);
        pedroApplication.setOwner(pedro);
        pedroApplication = this.jobApplicationRepository.save(pedroApplication);

        String accessToken = this.jwtService
                .generateToken("pedro@example.com")
                .getAccessToken();

        String requestBody = """
                {
                    "company": "Microsoft",
                    "position": "Backend Developer",
                    "status": "INTERVIEW"
                }
                """;

        this.mockMvc
                .perform(put("/api/applications/{id}", pedroApplication.getId())
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(pedroApplication.getId()))
                .andExpect(jsonPath("$.company").value("Microsoft"))
                .andExpect(jsonPath("$.position").value("Backend Developer"))
                .andExpect(jsonPath("$.status").value("INTERVIEW"));
    }
}