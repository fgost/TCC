package com.example.application.exception.errorType;

import com.example.application.exception.response.ErrorInfo;
import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GenericException extends RuntimeException {

    private ErrorInfo errorInfo;

    private HttpStatus status;
}
