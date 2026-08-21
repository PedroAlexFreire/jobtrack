package com.jobtrack.jobtrack.exception;

public class EmailAlreadyExistsException
        extends RuntimeException {

    public EmailAlreadyExistsException() {
        super("An account with this email already exists");
    }
}