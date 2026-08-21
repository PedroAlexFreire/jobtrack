package com.jobtrack.jobtrack.dto;

import java.util.Map;

public class ApiErrorResponse {

    private int status;
    private String message;
    private Map<String, String> errors;

    public ApiErrorResponse(
            int status,
            String message,
            Map<String, String> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public int getStatus() {
        return this.status;
    }

    public String getMessage() {
        return this.message;
    }

    public Map<String, String> getErrors() {
        return this.errors;
    }
}