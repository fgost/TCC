package com.example.application.exception.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ObjectNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final String code;
    private final String message;

    public ObjectNotFoundException() {
        this.code = String.valueOf(HttpStatus.NOT_FOUND.value());
        this.message = HttpStatus.NOT_FOUND.getReasonPhrase();
    }

    public ObjectNotFoundException(String message) {
        this.code = String.valueOf(HttpStatus.NOT_FOUND.value());
        this.message = message;
    }
}
