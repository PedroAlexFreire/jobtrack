package com.jobtrack.jobtrack.controller;

import com.jobtrack.jobtrack.model.JobApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobApplicationController {

    @GetMapping("/api/applications/sample")
    public JobApplication getSampleApplication() {
        // Criar e devolver uma JobApplication
        return new JobApplication(
        1L,
        "Microsoft",
        "Junior Java Developer",
        "APPLIED"
);
    }
}