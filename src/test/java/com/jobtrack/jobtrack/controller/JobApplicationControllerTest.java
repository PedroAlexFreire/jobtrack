package com.jobtrack.jobtrack.controller;

import com.jobtrack.jobtrack.model.ApplicationStatus;
import com.jobtrack.jobtrack.model.JobApplication;
import com.jobtrack.jobtrack.model.UserAccount;
import com.jobtrack.jobtrack.repository.JobApplicationRepository;
import com.jobtrack.jobtrack.repository.UserAccountRepository;
import com.jobtrack.jobtrack.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
                UserAccount pedro = this.createUser("Pedro", "pedro@example.com");
                UserAccount ana = this.createUser("Ana", "ana@example.com");

                this.createApplication(
                                pedro,
                                "Microsoft",
                                "Junior Java Developer",
                                ApplicationStatus.APPLIED);

                this.createApplication(
                                ana,
                                "Spotify",
                                "Backend Developer",
                                ApplicationStatus.INTERVIEW);

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
                                .andExpect(jsonPath("$[0].status").value("APPLIED"))
                                .andExpect(jsonPath("$[0].applicationDate").value("2026-08-30"));
        }

        @Test
        void getAllApplicationsReturnsUnauthorizedWithoutToken() throws Exception {
                this.mockMvc
                                .perform(get("/api/applications"))
                                .andExpect(status().isUnauthorized());
        }

        @Test
        void getApplicationByIdReturnsNotFoundForAnotherUsersApplication() throws Exception {
                this.createUser("Pedro", "pedro@example.com");
                UserAccount ana = this.createUser("Ana", "ana@example.com");

                JobApplication anaApplication = this.createApplication(
                                ana,
                                "Spotify",
                                "Backend Developer",
                                ApplicationStatus.INTERVIEW);

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
                this.createUser("Pedro", "pedro@example.com");
                UserAccount ana = this.createUser("Ana", "ana@example.com");

                JobApplication anaApplication = this.createApplication(
                                ana,
                                "Spotify",
                                "Backend Developer",
                                ApplicationStatus.INTERVIEW);

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
                this.createUser("Pedro", "pedro@example.com");
                UserAccount ana = this.createUser("Ana", "ana@example.com");

                JobApplication anaApplication = this.createApplication(
                                ana,
                                "Spotify",
                                "Backend Developer",
                                ApplicationStatus.INTERVIEW);

                String accessToken = this.jwtService
                                .generateToken("pedro@example.com")
                                .getAccessToken();

                String requestBody = """
                                {
                                    "company": "Changed",
                                    "position": "Changed Role",
                                    "status": "OFFER",
                                    "applicationDate": "2026-08-30"
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
                this.createUser("Pedro", "pedro@example.com");

                String accessToken = this.jwtService
                                .generateToken("pedro@example.com")
                                .getAccessToken();

                String requestBody = """
                                {
                                    "company": "Microsoft",
                                    "position": "Junior Java Developer",
                                    "status": "APPLIED",
                                    "applicationDate": "2026-08-30"
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
                                .andExpect(jsonPath("$.status").value("APPLIED"))
                                .andExpect(jsonPath("$.applicationDate").value("2026-08-30"));
        }

        @Test
        void addApplicationReturnsBadRequestWhenCompanyIsBlank() throws Exception {
                this.createUser("Pedro", "pedro@example.com");

                String accessToken = this.jwtService
                                .generateToken("pedro@example.com")
                                .getAccessToken();

                String requestBody = """
                                {
                                    "company": "",
                                    "position": "Junior Java Developer",
                                    "status": "APPLIED",
                                    "applicationDate": "2026-08-30"
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
        void addApplicationReturnsBadRequestWhenApplicationDateIsMissing() throws Exception {
                this.createUser("Pedro", "pedro@example.com");

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
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.status").value(400))
                                .andExpect(jsonPath("$.message").value("Validation failed"))
                                .andExpect(jsonPath("$.errors.applicationDate").value("Application date is required"));
        }

        @Test
        void addApplicationReturnsBadRequestWhenStatusIsInvalid() throws Exception {
                this.createUser("Pedro", "pedro@example.com");

                String accessToken = this.jwtService
                                .generateToken("pedro@example.com")
                                .getAccessToken();

                String requestBody = """
                                {
                                    "company": "Microsoft",
                                    "position": "Junior Java Developer",
                                    "status": "UNKNOWN",
                                    "applicationDate": "2026-08-30"
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
                UserAccount pedro = this.createUser("Pedro", "pedro@example.com");

                JobApplication pedroApplication = this.createApplication(
                                pedro,
                                "Microsoft",
                                "Junior Java Developer",
                                ApplicationStatus.APPLIED);

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
                UserAccount pedro = this.createUser("Pedro", "pedro@example.com");

                JobApplication pedroApplication = this.createApplication(
                                pedro,
                                "Microsoft",
                                "Junior Java Developer",
                                ApplicationStatus.APPLIED);

                String accessToken = this.jwtService
                                .generateToken("pedro@example.com")
                                .getAccessToken();

                String requestBody = """
                                {
                                    "company": "Microsoft",
                                    "position": "Backend Developer",
                                    "status": "INTERVIEW",
                                    "applicationDate": "2026-09-01"
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
                                .andExpect(jsonPath("$.status").value("INTERVIEW"))
                                .andExpect(jsonPath("$.applicationDate").value("2026-09-01"));
        }

        @Test
        void getApplicationByIdReturnsOwnedApplication() throws Exception {
                UserAccount pedro = this.createUser("Pedro", "pedro@example.com");

                JobApplication pedroApplication = this.createApplication(
                                pedro,
                                "Microsoft",
                                "Junior Java Developer",
                                ApplicationStatus.APPLIED);

                String accessToken = this.jwtService
                                .generateToken("pedro@example.com")
                                .getAccessToken();

                this.mockMvc
                                .perform(get("/api/applications/{id}", pedroApplication.getId())
                                                .header("Authorization", "Bearer " + accessToken))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(pedroApplication.getId()))
                                .andExpect(jsonPath("$.company").value("Microsoft"))
                                .andExpect(jsonPath("$.position").value("Junior Java Developer"))
                                .andExpect(jsonPath("$.status").value("APPLIED"))
                                .andExpect(jsonPath("$.applicationDate").value("2026-08-30"));
        }

        @Test
        void getApplicationByIdReturnsNotFoundWhenApplicationDoesNotExist() throws Exception {
                this.createUser("Pedro", "pedro@example.com");

                String accessToken = this.jwtService
                                .generateToken("pedro@example.com")
                                .getAccessToken();

                this.mockMvc
                                .perform(get("/api/applications/{id}", 999L)
                                                .header("Authorization", "Bearer " + accessToken))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.status").value(404))
                                .andExpect(jsonPath("$.message").value("Job application with id 999 was not found"));
        }

        @Test
        void addApplicationReturnsUnauthorizedWithoutToken() throws Exception {
                String requestBody = """
                                {
                                    "company": "Microsoft",
                                    "position": "Junior Java Developer",
                                    "status": "APPLIED",
                                    "applicationDate": "2026-08-30"
                                }
                                """;

                this.mockMvc
                                .perform(post("/api/applications")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(requestBody))
                                .andExpect(status().isUnauthorized());
        }

        @Test
        void updateApplicationReturnsUnauthorizedWithoutToken() throws Exception {
                String requestBody = """
                                {
                                    "company": "Microsoft",
                                    "position": "Backend Developer",
                                    "status": "INTERVIEW",
                                    "applicationDate": "2026-09-01"
                                }
                                """;

                this.mockMvc
                                .perform(put("/api/applications/{id}", 1L)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(requestBody))
                                .andExpect(status().isUnauthorized());
        }

        @Test
        void deleteApplicationReturnsUnauthorizedWithoutToken() throws Exception {
                this.mockMvc
                                .perform(delete("/api/applications/{id}", 1L))
                                .andExpect(status().isUnauthorized());
        }

        private UserAccount createUser(
                        String name,
                        String email) {
                return this.userAccountRepository.save(
                                new UserAccount(
                                                null,
                                                name,
                                                email,
                                                "{noop}password"));
        }

        private JobApplication createApplication(
                        UserAccount owner,
                        String company,
                        String position,
                        ApplicationStatus status) {
                JobApplication application = new JobApplication(
                                null,
                                company,
                                position,
                                status,
                                LocalDate.parse("2026-08-30"));

                application.setOwner(owner);

                return this.jobApplicationRepository.save(application);
        }
}