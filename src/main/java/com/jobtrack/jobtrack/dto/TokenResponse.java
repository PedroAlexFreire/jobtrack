package com.jobtrack.jobtrack.dto;

public class TokenResponse {

    private String accessToken;
    private String tokenType;
    private long expiresIn;

    public TokenResponse(
            String accessToken,
            String tokenType,
            long expiresIn) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public String getTokenType() {
        return this.tokenType;
    }

    public long getExpiresIn() {
        return this.expiresIn;
    }
}