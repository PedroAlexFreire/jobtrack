package com.jobtrack.jobtrack.service;

import com.jobtrack.jobtrack.model.JobApplication;
import com.jobtrack.jobtrack.repository.JobApplicationRepository;
import com.jobtrack.jobtrack.model.UserAccount;
import com.jobtrack.jobtrack.repository.UserAccountRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobApplicationService {

    private final JobApplicationRepository repository;
    private final UserAccountRepository userAccountRepository;

    public JobApplicationService(
            JobApplicationRepository repository,
            UserAccountRepository userAccountRepository) {

        this.repository = repository;
        this.userAccountRepository = userAccountRepository;
    }

    public List<JobApplication> getAllApplications(String authenticatedEmail) {
        return this.repository.findAllByOwner_Email(authenticatedEmail);
    }

    public JobApplication addApplication(
            JobApplication application,
            String authenticatedEmail) {

        UserAccount owner = this.userAccountRepository
                .findByEmail(authenticatedEmail)
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found"));

        application.setId(null);
        application.setOwner(owner);

        return this.repository.save(application);
    }

    public JobApplication getApplicationById(
            Long id,
            String authenticatedEmail) {

        return this.repository
                .findByIdAndOwner_Email(id, authenticatedEmail)
                .orElse(null);
    }

    public boolean deleteApplication(
            Long id,
            String authenticatedEmail) {

        JobApplication application = this.getApplicationById(id, authenticatedEmail);

        if (application == null) {
            return false;
        }

        this.repository.delete(application);
        return true;
    }

    public JobApplication updateApplication(
            Long id,
            JobApplication updatedApplication,
            String authenticatedEmail) {
        JobApplication existingApplication = this.getApplicationById(id, authenticatedEmail);

        if (existingApplication == null) {
            return null;
        }

        existingApplication.setCompany(updatedApplication.getCompany());
        existingApplication.setPosition(updatedApplication.getPosition());
        existingApplication.setStatus(updatedApplication.getStatus());

        return this.repository.save(existingApplication);
    }
}