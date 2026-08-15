package com.jobtrack.jobtrack.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "job_applications")
public class JobApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Company name is required")
    private String company;
    @NotBlank(message = "Position is required")
    private String position;
    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    public JobApplication() {

    }

    public JobApplication(Long id, String company, String position, ApplicationStatus status) {
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

    public void setId(Long id) {
        this.id = id;
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
}
