package com.jobtrack.jobtrack.model;

public class JobApplication {
    private Long id;
    private String company;
    private String position;
    private String status;

    public JobApplication() {

    }

    public JobApplication(Long id, String company, String position, String status) {
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

    public String getStatus() {
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

    public void setStatus(String status) {
        this.status = status;
    }
}
