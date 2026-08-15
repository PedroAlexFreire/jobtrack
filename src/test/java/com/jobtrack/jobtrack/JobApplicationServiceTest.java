package com.jobtrack.jobtrack;

import com.jobtrack.jobtrack.model.JobApplication;
import com.jobtrack.jobtrack.repository.JobApplicationRepository;
import com.jobtrack.jobtrack.service.JobApplicationService;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import com.jobtrack.jobtrack.model.ApplicationStatus;

class JobApplicationServiceTest {

    @Test
    void getAllApplicationsReturnsRepositoryApplications() {
        JobApplicationRepository repository =
                mock(JobApplicationRepository.class);

        JobApplicationService service =
                new JobApplicationService(repository);

        List<JobApplication> storedApplications = List.of(
                new JobApplication(
                        1L,
                        "Microsoft",
                        "Junior Java Developer",
                        ApplicationStatus.APPLIED
                ),
                new JobApplication(
                        2L,
                        "Spotify",
                        "Backend Developer",
                        ApplicationStatus.INTERVIEW
                )
        );

        when(repository.findAll()).thenReturn(storedApplications);

        List<JobApplication> result = service.getAllApplications();

        assertEquals(2, result.size());
    }

    @Test
    void getApplicationByIdReturnsMatchingApplication() {
        JobApplicationRepository repository =
                mock(JobApplicationRepository.class);

        JobApplicationService service =
                new JobApplicationService(repository);

        JobApplication storedApplication = new JobApplication(
                1L,
                "Microsoft",
                "Junior Java Developer",
                ApplicationStatus.APPLIED
        );

        when(repository.findById(1L))
                .thenReturn(Optional.of(storedApplication));

        JobApplication result = service.getApplicationById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Microsoft", result.getCompany());
    }

    @Test
    void getApplicationByIdReturnsNullWhenApplicationDoesNotExist() {
        JobApplicationRepository repository =
                mock(JobApplicationRepository.class);

        JobApplicationService service =
                new JobApplicationService(repository);

        when(repository.findById(999L))
                .thenReturn(Optional.empty());

        JobApplication result = service.getApplicationById(999L);

        assertNull(result);
    }
}