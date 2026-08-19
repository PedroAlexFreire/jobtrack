package com.jobtrack.jobtrack.service;

import com.jobtrack.jobtrack.model.ApplicationStatus;
import com.jobtrack.jobtrack.model.JobApplication;
import com.jobtrack.jobtrack.repository.JobApplicationRepository;
import com.jobtrack.jobtrack.repository.UserAccountRepository;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JobApplicationServiceTest {

        @Test
        void getAllApplicationsReturnsRepositoryApplications() {
                JobApplicationRepository repository = mock(JobApplicationRepository.class);

                UserAccountRepository userAccountRepository = mock(UserAccountRepository.class);

                JobApplicationService service = new JobApplicationService(
                                repository,
                                userAccountRepository);

                List<JobApplication> storedApplications = List.of(
                                new JobApplication(
                                                1L,
                                                "Microsoft",
                                                "Junior Java Developer",
                                                ApplicationStatus.APPLIED),
                                new JobApplication(
                                                2L,
                                                "Spotify",
                                                "Backend Developer",
                                                ApplicationStatus.INTERVIEW));

                when(repository.findAllByOwner_Email("pedro@example.com"))
                                .thenReturn(storedApplications);

                List<JobApplication> result = service.getAllApplications("pedro@example.com");

                assertEquals(2, result.size());
        }

        @Test
        void getApplicationByIdReturnsMatchingApplication() {
                JobApplicationRepository repository = mock(JobApplicationRepository.class);

                UserAccountRepository userAccountRepository = mock(UserAccountRepository.class);

                JobApplicationService service = new JobApplicationService(
                                repository,
                                userAccountRepository);

                JobApplication storedApplication = new JobApplication(
                                1L,
                                "Microsoft",
                                "Junior Java Developer",
                                ApplicationStatus.APPLIED);

                when(repository.findByIdAndOwner_Email(
                                1L,
                                "pedro@example.com")).thenReturn(Optional.of(storedApplication));

                JobApplication result = service.getApplicationById(
                                1L,
                                "pedro@example.com");

                assertNotNull(result);
                assertEquals(1L, result.getId());
                assertEquals("Microsoft", result.getCompany());
        }

        @Test
        void getApplicationByIdReturnsNullWhenApplicationDoesNotExist() {
                JobApplicationRepository repository = mock(JobApplicationRepository.class);

                UserAccountRepository userAccountRepository = mock(UserAccountRepository.class);

                JobApplicationService service = new JobApplicationService(
                                repository,
                                userAccountRepository);

                when(repository.findByIdAndOwner_Email(
                                999L,
                                "pedro@example.com")).thenReturn(Optional.empty());

                JobApplication result = service.getApplicationById(
                                999L,
                                "pedro@example.com");

                assertNull(result);
        }
}