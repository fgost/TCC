package com.example.application.exception.domain;

import com.example.application.exception.configuration.ConstantsExceptions;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuditException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private final String code;
    private final String message;

    public AuditException() {
        this.code = ConstantsExceptions.EXCEPTION_CODE_AUDIT;
        this.message = "Audit Error";
    }

    public AuditException(String message) {
        this.code = ConstantsExceptions.EXCEPTION_CODE_AUDIT;
        this.message = message;
    }
}
