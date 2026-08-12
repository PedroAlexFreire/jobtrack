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
import java.util.List;

@RestController
public class JobApplicationController {
    private final JobApplicationService service;

    public JobApplicationController(JobApplicationService service) {
        this.service = service;

    }

    @GetMapping("/api/applications")
    public List<JobApplication> getAllApplications() {
        return this.service.getAllApplications();
    }

    @PostMapping("/api/applications")
    public JobApplication addApplication(@RequestBody JobApplication application) {
        return this.service.addApplication(application);
    }

    @GetMapping("/api/applications/{id}")
    public ResponseEntity<JobApplication> getApplicationById(@PathVariable Long id) {
        JobApplication application = this.service.getApplicationById(id);

        if (application == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(application);
    }

    @DeleteMapping("/api/applications/{id}")
    public ResponseEntity<Void> deleteApplication(@PathVariable Long id) {
        boolean removed = this.service.deleteApplication(id);

        if (!removed) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/api/applications/{id}")
    public ResponseEntity<JobApplication> updateApplication(
            @PathVariable Long id,
            @RequestBody JobApplication updatedApplication) {
        JobApplication result = this.service.updateApplication(id, updatedApplication);

        if (result == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(result);
    }
}