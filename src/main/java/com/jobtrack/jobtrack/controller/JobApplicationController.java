package com.jobtrack.jobtrack.controller;

import com.jobtrack.jobtrack.model.JobApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.ArrayList;
import java.util.List;

@RestController
public class JobApplicationController {
    private final List<JobApplication> applications;
    public JobApplicationController() {
        this.applications = new ArrayList<>();

        this.applications.add(
            new JobApplication(1L, "Microsoft", "Junior Java Developer", "APPLIED")
        );

        this.applications.add(
            new JobApplication(2L, "Spotify", "Backend Developer", "INTERVIEW")
        );
    }

    @GetMapping("/api/applications")
    public List<JobApplication> getAllApplications() {
        return this.applications;
    }
    @PostMapping("/api/applications")
    public JobApplication addApplication(@RequestBody JobApplication application) {
        this.applications.add(application);
        return application;
    }
    @GetMapping("/api/applications/{id}")
    public ResponseEntity<JobApplication> getApplicationById(@PathVariable Long id) {

        for (JobApplication application : this.applications) {
            if (application.getId().equals(id)) {
                return ResponseEntity.ok(application);
            }
        }

        return ResponseEntity.notFound().build();
    }
}