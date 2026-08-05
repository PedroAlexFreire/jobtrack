package com.jobtrack.jobtrack.controller;

import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class HealthController {

    @GetMapping("/api/health")
    public String health() {
        return "JobTrack is running";
    }
    
}
