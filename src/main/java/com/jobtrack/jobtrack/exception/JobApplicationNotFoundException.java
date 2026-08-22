package com.jobtrack.jobtrack.exception;

public class JobApplicationNotFoundException
        extends RuntimeException {

    public JobApplicationNotFoundException(Long id) {
        super("Job application with id " + id + " was not found");
    }
}