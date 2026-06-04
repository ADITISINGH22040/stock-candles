package com.stockcandles.exception;

import java.time.Instant;

public class ErrorResponse {

    private final String errorCode;
    private final String message;
    private final Instant timestamp;

    public ErrorResponse(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
        this.timestamp = Instant.now();
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
