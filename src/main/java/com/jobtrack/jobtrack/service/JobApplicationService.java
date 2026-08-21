package com.jobtrack.jobtrack.service;

import com.jobtrack.jobtrack.model.JobApplication;
import com.jobtrack.jobtrack.repository.JobApplicationRepository;
import com.jobtrack.jobtrack.model.UserAccount;
import com.jobtrack.jobtrack.repository.UserAccountRepository;
import com.jobtrack.jobtrack.dto.JobApplicationRequest;
import com.jobtrack.jobtrack.dto.JobApplicationResponse;
import java.util.ArrayList;

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

    public List<JobApplicationResponse> getAllApplications(
            String authenticatedEmail) {

        List<JobApplication> applications = this.repository.findAllByOwner_Email(
                authenticatedEmail);

        List<JobApplicationResponse> responses = new ArrayList<>();

        for (JobApplication application : applications) {
            responses.add(this.toResponse(application));
        }

        return responses;
    }

    public JobApplicationResponse addApplication(
            JobApplicationRequest request,
            String authenticatedEmail) {

        UserAccount owner = this.userAccountRepository
                .findByEmail(authenticatedEmail)
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found"));

        JobApplication application = new JobApplication(
                null,
                request.getCompany(),
                request.getPosition(),
                request.getStatus());

        application.setOwner(owner);

        JobApplication savedApplication = this.repository.save(application);

        return this.toResponse(savedApplication);
    }

    public JobApplicationResponse getApplicationById(
            Long id,
            String authenticatedEmail) {

        JobApplication application = this.findOwnedApplication(
                id,
                authenticatedEmail);

        if (application == null) {
            return null;
        }

        return this.toResponse(application);
    }

    public boolean deleteApplication(
            Long id,
            String authenticatedEmail) {

        JobApplication application = this.findOwnedApplication(id, authenticatedEmail);

        if (application == null) {
            return false;
        }

        this.repository.delete(application);
        return true;
    }

    public JobApplicationResponse updateApplication(
            Long id,
            JobApplicationRequest request,
            String authenticatedEmail) {
        JobApplication existingApplication = this.findOwnedApplication(id, authenticatedEmail);

        if (existingApplication == null) {
            return null;
        }

        existingApplication.setCompany(request.getCompany());
        existingApplication.setPosition(request.getPosition());
        existingApplication.setStatus(request.getStatus());

        JobApplication savedApplication = this.repository.save(existingApplication);

        return this.toResponse(savedApplication);
    }

    private JobApplication findOwnedApplication(
            Long id,
            String authenticatedEmail) {

        return this.repository
                .findByIdAndOwner_Email(
                        id,
                        authenticatedEmail)
                .orElse(null);
    }

    private JobApplicationResponse toResponse(
            JobApplication application) {

        return new JobApplicationResponse(
                application.getId(),
                application.getCompany(),
                application.getPosition(),
                application.getStatus());
    }
}