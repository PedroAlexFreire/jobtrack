package com.jobtrack.jobtrack.repository;

import com.jobtrack.jobtrack.model.ApplicationStatus;
import com.jobtrack.jobtrack.model.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface JobApplicationRepository
                extends JpaRepository<JobApplication, Long> {

        List<JobApplication> findAllByOwner_Email(String email);

        List<JobApplication> findAllByOwner_EmailAndStatus(
        String email,
        ApplicationStatus status);

        Optional<JobApplication> findByIdAndOwner_Email(
        Long id,
        String email
);
}
