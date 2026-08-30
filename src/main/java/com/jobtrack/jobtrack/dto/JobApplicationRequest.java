package com.jobtrack.jobtrack.dto;

import java.time.LocalDate;

import com.jobtrack.jobtrack.model.ApplicationStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class JobApplicationRequest {

    @NotBlank(message = "Company name is required")
    private String company;

    @NotBlank(message = "Position is required")
    private String position;

    @NotNull(message = "Status is required")
    private ApplicationStatus status;

    public JobApplicationRequest() {
    }

    public JobApplicationRequest(
            String company,
            String position,
            ApplicationStatus status,
            LocalDate applicationDate) {
        this.company = company;
        this.position = position;
        this.status = status;
        this.applicationDate = applicationDate;
    }

    public String getCompany() {
        return this.company;
    }

    public String getPosition() {
        return this.position;
    }

    public ApplicationStatus getStatus() {
        return this.status;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public LocalDate getApplicationDate() {
        return this.applicationDate;
    }

    public void setApplicationDate(LocalDate applicationDate) {
        this.applicationDate = applicationDate;
    }

    @NotNull(message = "Application date is required")
    private LocalDate applicationDate;
}