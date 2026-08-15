package com.jobtrack.jobtrack.service;

import com.jobtrack.jobtrack.model.JobApplication;
import com.jobtrack.jobtrack.repository.JobApplicationRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobApplicationService {

    private final JobApplicationRepository repository;

    public JobApplicationService(JobApplicationRepository repository) {
        this.repository = repository;
    }

    public List<JobApplication> getAllApplications() {
        return this.repository.findAll();
    }

    public JobApplication addApplication(JobApplication application) {
        application.setId(null);
        return this.repository.save(application);
    }

    public JobApplication getApplicationById(Long id) {
        return this.repository.findById(id).orElse(null);
    }

    public boolean deleteApplication(Long id) {
        if (!this.repository.existsById(id)) {
            return false;
        }

        this.repository.deleteById(id);
        return true;
    }

    public JobApplication updateApplication(
            Long id,
            JobApplication updatedApplication
    ) {
        JobApplication existingApplication =
                this.getApplicationById(id);

        if (existingApplication == null) {
            return null;
        }

        existingApplication.setCompany(updatedApplication.getCompany());
        existingApplication.setPosition(updatedApplication.getPosition());
        existingApplication.setStatus(updatedApplication.getStatus());

        return this.repository.save(existingApplication);
    }
}