package com.jobtrack.jobtrack.controller;

import com.jobtrack.jobtrack.service.JobApplicationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.security.core.Authentication;
import com.jobtrack.jobtrack.dto.JobApplicationRequest;
import com.jobtrack.jobtrack.dto.JobApplicationResponse;
import java.util.List;
import jakarta.validation.Valid;

@RestController
public class JobApplicationController {
        private final JobApplicationService service;

        public JobApplicationController(JobApplicationService service) {
                this.service = service;

        }

        @GetMapping("/api/applications")
        public List<JobApplicationResponse> getAllApplications(
                        Authentication authentication) {

                return this.service.getAllApplications(
                                authentication.getName());
        }

        @PostMapping("/api/applications")
        public ResponseEntity<JobApplicationResponse> addApplication(
                        @Valid @RequestBody JobApplicationRequest request,
                        Authentication authentication) {

                JobApplicationResponse response = this.service.addApplication(
                                request,
                                authentication.getName());

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(response);
        }

        @GetMapping("/api/applications/{id}")
        public ResponseEntity<JobApplicationResponse> getApplicationById(
                        @PathVariable Long id,
                        Authentication authentication) {

                JobApplicationResponse response = this.service.getApplicationById(
                                id,
                                authentication.getName());

                return ResponseEntity.ok(response);
        }

        @DeleteMapping("/api/applications/{id}")
        public ResponseEntity<Void> deleteApplication(
                        @PathVariable Long id,
                        Authentication authentication) {

                this.service.deleteApplication(
                                id,
                                authentication.getName());

                return ResponseEntity.noContent().build();
        }

        @PutMapping("/api/applications/{id}")
        public ResponseEntity<JobApplicationResponse> updateApplication(
                        @PathVariable Long id,
                        @Valid @RequestBody JobApplicationRequest request,
                        Authentication authentication) {

                JobApplicationResponse response = this.service.updateApplication(
                                id,
                                request,
                                authentication.getName());

                return ResponseEntity.ok(response);
        }
}