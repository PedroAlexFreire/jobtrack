package com.jobtrack.jobtrack.service;

import com.jobtrack.jobtrack.model.JobApplication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JobApplicationService {

    private final List<JobApplication> applications;

    public JobApplicationService() {
        this.applications = new ArrayList<>();

        this.applications.add(
                new JobApplication(1L, "Microsoft", "Junior Java Developer", "APPLIED"));

        this.applications.add(
                new JobApplication(2L, "Spotify", "Backend Developer", "INTERVIEW"));
    }

    public List<JobApplication> getAllApplications() {
        return this.applications;
    }

    public JobApplication addApplication(JobApplication application) {
        this.applications.add(application);
        return application;
    }

    public JobApplication getApplicationById(Long id) {
        for (JobApplication application : this.applications) {
            if (application.getId().equals(id)) {
                return application;
            }
        }

        return null;
    }

    public boolean deleteApplication(Long id) {
        for (int i = 0; i < this.applications.size(); i++) {
            JobApplication application = this.applications.get(i);

            if (application.getId().equals(id)) {
                this.applications.remove(i);
                return true;
            }
        }

        return false;
    }

    public JobApplication updateApplication(
            Long id,
            JobApplication updatedApplication) {
        JobApplication existingApplication = this.getApplicationById(id);

        if (existingApplication == null) {
            return null;
        }

        existingApplication.setCompany(updatedApplication.getCompany());
        existingApplication.setPosition(updatedApplication.getPosition());
        existingApplication.setStatus(updatedApplication.getStatus());

        return existingApplication;
    }
}