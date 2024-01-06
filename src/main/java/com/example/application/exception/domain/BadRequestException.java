package com.example.application.exception.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class BadRequestException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String code;

    private final String message;

    public BadRequestException() {
        this.code = HttpStatus.BAD_REQUEST.toString();
        this.message = "Unprocessed";
    }

    public BadRequestException(String message) {
        this.code = HttpStatus.BAD_REQUEST.toString();
        this.message = message;
    }
}
