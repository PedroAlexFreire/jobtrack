package com.jobtrack.jobtrack.dto;

import com.jobtrack.jobtrack.model.ApplicationStatus;

public class JobApplicationResponse {

    private Long id;
    private String company;
    private String position;
    private ApplicationStatus status;

    public JobApplicationResponse(
            Long id,
            String company,
            String position,
            ApplicationStatus status
    ) {
        this.id = id;
        this.company = company;
        this.position = position;
        this.status = status;
    }

    public Long getId() {
        return this.id;
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
}