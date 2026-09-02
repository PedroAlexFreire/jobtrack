package com.jobtrack.jobtrack.service;

import com.jobtrack.jobtrack.dto.JobApplicationRequest;
import com.jobtrack.jobtrack.dto.JobApplicationResponse;
import com.jobtrack.jobtrack.exception.JobApplicationNotFoundException;
import com.jobtrack.jobtrack.model.ApplicationStatus;
import com.jobtrack.jobtrack.model.JobApplication;
import com.jobtrack.jobtrack.model.UserAccount;
import com.jobtrack.jobtrack.repository.JobApplicationRepository;
import com.jobtrack.jobtrack.repository.UserAccountRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

        public Page<JobApplicationResponse> getAllApplications(
                        String authenticatedEmail,
                        ApplicationStatus status,
                        String search,
                        Pageable pageable) {

                Page<JobApplication> applications;

                boolean hasSearch = search != null && !search.isBlank();

                if (status == null && !hasSearch) {
                        applications = this.repository.findAllByOwner_Email(
                                        authenticatedEmail,
                                        pageable);
                } else if (status != null && !hasSearch) {
                        applications = this.repository.findAllByOwner_EmailAndStatus(
                                        authenticatedEmail,
                                        status,
                                        pageable);
                } else if (status == null) {
                        applications = this.repository.searchByOwnerEmail(
                                        authenticatedEmail,
                                        search.trim(),
                                        pageable);
                } else {
                        applications = this.repository.searchByOwnerEmailAndStatus(
                                        authenticatedEmail,
                                        status,
                                        search.trim(),
                                        pageable);
                }

                return applications.map(this::toResponse);
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
                                request.getStatus(),
                                request.getApplicationDate());

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

                return this.toResponse(application);
        }

        public void deleteApplication(
                        Long id,
                        String authenticatedEmail) {

                JobApplication application = this.findOwnedApplication(
                                id,
                                authenticatedEmail);

                this.repository.delete(application);
        }

        public JobApplicationResponse updateApplication(
                        Long id,
                        JobApplicationRequest request,
                        String authenticatedEmail) {

                JobApplication existingApplication = this.findOwnedApplication(id, authenticatedEmail);

                existingApplication.setCompany(request.getCompany());
                existingApplication.setPosition(request.getPosition());
                existingApplication.setStatus(request.getStatus());
                existingApplication.setApplicationDate(request.getApplicationDate());

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
                                .orElseThrow(() -> new JobApplicationNotFoundException(id));
        }

        private JobApplicationResponse toResponse(
                        JobApplication application) {

                return new JobApplicationResponse(
                                application.getId(),
                                application.getCompany(),
                                application.getPosition(),
                                application.getStatus(),
                                application.getApplicationDate());
        }
}