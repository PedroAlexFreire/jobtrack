package com.jobtrack.jobtrack.repository;

import com.jobtrack.jobtrack.model.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobApplicationRepository
        extends JpaRepository<JobApplication, Long> {
}

