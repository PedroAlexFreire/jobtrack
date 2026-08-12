package com.jobtrack.jobtrack;

import org.junit.jupiter.api.Test;

import com.jobtrack.jobtrack.model.JobApplication;
import com.jobtrack.jobtrack.service.JobApplicationService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import com.jobtrack.jobtrack.model.JobApplication;

class JobApplicationServiceTest {

    @Test
    void getAllApplicationsReturnsInitialApplications() {
        JobApplicationService service = new JobApplicationService();

        int numberOfApplications = service.getAllApplications().size();

        assertEquals(2, numberOfApplications);
    }

    @Test
    void getApplicationByIdReturnsMatchingApplication() {
        JobApplicationService service = new JobApplicationService();

        JobApplication application = service.getApplicationById(1L);

        assertNotNull(application);
        assertEquals(1L, application.getId());
        assertEquals("Microsoft", application.getCompany());
    }

    @Test
    void getApplicationByIdReturnsNullWhenApplicationDoesNotExist() {
        JobApplicationService service = new JobApplicationService();

        JobApplication application = service.getApplicationById(999L);

        assertNull(application);
    }

    @Test
    void addApplicationGeneratesSequentialIds() {
        JobApplicationService service = new JobApplicationService();

        JobApplication firstApplication = new JobApplication(
                null,
                "Blip",
                "Junior Backend Developer",
                "APPLIED");

        JobApplication secondApplication = new JobApplication(
                null,
                "Critical Software",
                "Junior Java Developer",
                "APPLIED");

        JobApplication firstResult = service.addApplication(firstApplication);

        JobApplication secondResult = service.addApplication(secondApplication);

        assertEquals(3L, firstResult.getId());
        assertEquals(4L, secondResult.getId());
        assertEquals(4, service.getAllApplications().size());
    }
}