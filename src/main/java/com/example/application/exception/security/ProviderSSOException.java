package com.example.application.exception.security;

public class ProviderSSOException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ProviderSSOException(String message) {
        super(message);
    }

    public ProviderSSOException(String message, Throwable cause) {
        super(message, cause);
    }
}
