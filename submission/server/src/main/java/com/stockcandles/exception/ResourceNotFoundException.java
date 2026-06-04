package com.stockcandles.exception;

public class ResourceNotFoundException extends ApiException {

    public ResourceNotFoundException(String message) {
        super("NOT_FOUND", message);
    }
}
