package com.example.application.exception.domain;

import org.springframework.http.HttpStatus;

public class NotPersistedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String code;

    private final String message;

    public NotPersistedException() {
        this.code = HttpStatus.UNPROCESSABLE_ENTITY.toString();
        this.message = "Unprocessed";
    }

    public NotPersistedException(String message) {
        this.code = HttpStatus.UNPROCESSABLE_ENTITY.toString();
        this.message = message;
    }
}
