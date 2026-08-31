package com.jobtrack.jobtrack.service;

import com.jobtrack.jobtrack.dto.JobApplicationRequest;
import com.jobtrack.jobtrack.dto.JobApplicationResponse;
import com.jobtrack.jobtrack.exception.JobApplicationNotFoundException;
import com.jobtrack.jobtrack.model.ApplicationStatus;
import com.jobtrack.jobtrack.model.JobApplication;
import com.jobtrack.jobtrack.repository.JobApplicationRepository;
import com.jobtrack.jobtrack.repository.UserAccountRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
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
                                                ApplicationStatus.APPLIED,
                                                LocalDate.parse("2026-08-30")),
                                new JobApplication(
                                                2L,
                                                "Spotify",
                                                "Backend Developer",
                                                ApplicationStatus.INTERVIEW,
                                                LocalDate.parse("2026-08-31")));

                when(repository.findAllByOwner_Email("pedro@example.com"))
                                .thenReturn(storedApplications);

                List<JobApplicationResponse> result = service.getAllApplications(
                                "pedro@example.com",
                                null);

                assertEquals(2, result.size());
                assertEquals(1L, result.get(0).getId());
                assertEquals("Microsoft", result.get(0).getCompany());
                assertEquals(LocalDate.parse("2026-08-30"), result.get(0).getApplicationDate());
                assertEquals(2L, result.get(1).getId());
                assertEquals("Spotify", result.get(1).getCompany());
                assertEquals(LocalDate.parse("2026-08-31"), result.get(1).getApplicationDate());
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
                                ApplicationStatus.APPLIED,
                                LocalDate.parse("2026-08-30"));

                when(repository.findByIdAndOwner_Email(
                                1L,
                                "pedro@example.com")).thenReturn(Optional.of(storedApplication));

                JobApplicationResponse result = service.getApplicationById(
                                1L,
                                "pedro@example.com");

                assertNotNull(result);
                assertEquals(1L, result.getId());
                assertEquals("Microsoft", result.getCompany());
                assertEquals(LocalDate.parse("2026-08-30"), result.getApplicationDate());
        }

        @Test
        void getApplicationByIdThrowsExceptionWhenApplicationDoesNotExist() {
                JobApplicationRepository repository = mock(JobApplicationRepository.class);
                UserAccountRepository userAccountRepository = mock(UserAccountRepository.class);

                JobApplicationService service = new JobApplicationService(
                                repository,
                                userAccountRepository);

                when(repository.findByIdAndOwner_Email(
                                999L,
                                "pedro@example.com")).thenReturn(Optional.empty());

                JobApplicationNotFoundException exception = assertThrows(
                                JobApplicationNotFoundException.class,
                                () -> service.getApplicationById(
                                                999L,
                                                "pedro@example.com"));

                assertEquals(
                                "Job application with id 999 was not found",
                                exception.getMessage());
        }

        @Test
        void updateApplicationUpdatesAndReturnsApplication() {
                JobApplicationRepository repository = mock(JobApplicationRepository.class);
                UserAccountRepository userAccountRepository = mock(UserAccountRepository.class);

                JobApplicationService service = new JobApplicationService(
                                repository,
                                userAccountRepository);

                JobApplication storedApplication = new JobApplication(
                                1L,
                                "Microsoft",
                                "Junior Java Developer",
                                ApplicationStatus.APPLIED,
                                LocalDate.parse("2026-08-30"));

                JobApplicationRequest request = new JobApplicationRequest(
                                "Microsoft",
                                "Backend Developer",
                                ApplicationStatus.INTERVIEW,
                                LocalDate.parse("2026-09-01"));

                when(repository.findByIdAndOwner_Email(
                                1L,
                                "pedro@example.com")).thenReturn(Optional.of(storedApplication));

                when(repository.save(storedApplication))
                                .thenReturn(storedApplication);

                JobApplicationResponse result = service.updateApplication(
                                1L,
                                request,
                                "pedro@example.com");

                assertEquals(1L, result.getId());
                assertEquals("Microsoft", result.getCompany());
                assertEquals("Backend Developer", result.getPosition());
                assertEquals(
                                ApplicationStatus.INTERVIEW,
                                result.getStatus());
                assertEquals(LocalDate.parse("2026-09-01"), result.getApplicationDate());

                verify(repository).save(storedApplication);
        }

        @Test
        void deleteApplicationDeletesOwnedApplication() {
                JobApplicationRepository repository = mock(JobApplicationRepository.class);
                UserAccountRepository userAccountRepository = mock(UserAccountRepository.class);

                JobApplicationService service = new JobApplicationService(
                                repository,
                                userAccountRepository);

                JobApplication storedApplication = new JobApplication(
                                1L,
                                "Microsoft",
                                "Junior Java Developer",
                                ApplicationStatus.APPLIED,
                                LocalDate.parse("2026-08-30"));

                when(repository.findByIdAndOwner_Email(
                                1L,
                                "pedro@example.com")).thenReturn(Optional.of(storedApplication));

                service.deleteApplication(
                                1L,
                                "pedro@example.com");

                verify(repository).delete(storedApplication);
        }

        @Test
        void deleteApplicationDoesNotDeleteWhenApplicationIsNotOwned() {
                JobApplicationRepository repository = mock(JobApplicationRepository.class);
                UserAccountRepository userAccountRepository = mock(UserAccountRepository.class);

                JobApplicationService service = new JobApplicationService(
                                repository,
                                userAccountRepository);

                when(repository.findByIdAndOwner_Email(
                                10L,
                                "pedro@example.com")).thenReturn(Optional.empty());

                JobApplicationNotFoundException exception = assertThrows(
                                JobApplicationNotFoundException.class,
                                () -> service.deleteApplication(
                                                10L,
                                                "pedro@example.com"));

                assertEquals(
                                "Job application with id 10 was not found",
                                exception.getMessage());

                verify(repository, never())
                                .delete(any(JobApplication.class));
        }
}