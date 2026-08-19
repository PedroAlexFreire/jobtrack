package com.jobtrack.jobtrack.controller;

import com.jobtrack.jobtrack.model.JobApplication;
import com.jobtrack.jobtrack.service.JobApplicationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.security.core.Authentication;
import java.util.List;
import jakarta.validation.Valid;

@RestController
public class JobApplicationController {
    private final JobApplicationService service;

    public JobApplicationController(JobApplicationService service) {
        this.service = service;

    }

    @GetMapping("/api/applications")
    public List<JobApplication> getAllApplications(
            Authentication authentication) {

        return this.service.getAllApplications(
                authentication.getName());
    }

    @PostMapping("/api/applications")
    public JobApplication addApplication(
            @Valid @RequestBody JobApplication application,
            Authentication authentication) {

        return this.service.addApplication(
                application,
                authentication.getName());
    }

    @GetMapping("/api/applications/{id}")
    public ResponseEntity<JobApplication> getApplicationById(
            @PathVariable Long id,
            Authentication authentication) {

        JobApplication application = this.service.getApplicationById(
                id,
                authentication.getName());

        if (application == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(application);
    }

    @DeleteMapping("/api/applications/{id}")
    public ResponseEntity<Void> deleteApplication(
            @PathVariable Long id,
            Authentication authentication) {

        boolean removed = this.service.deleteApplication(
                id,
                authentication.getName());

        if (!removed) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/api/applications/{id}")
    public ResponseEntity<JobApplication> updateApplication(
            @PathVariable Long id,
            @Valid @RequestBody JobApplication updatedApplication,
            Authentication authentication) {

        JobApplication result = this.service.updateApplication(
                id,
                updatedApplication,
                authentication.getName());

        if (result == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(result);
    }
}