package com.example.application.exception;

import com.example.application.exception.domain.*;
import com.example.application.exception.errorType.GenericException;
import com.example.application.exception.model.response.ExceptionResponse;
import com.example.application.exception.response.ErrorSpec;
import com.example.application.exception.security.ProviderSSOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@ControllerAdvice
public class DefaultResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleAllException(Exception exception) {
        log.error("HANDLING >> Generic exception", exception);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ExceptionResponse(HttpStatus.BAD_REQUEST.toString(),
                        exception.getMessage(),
                        exception.toString()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception) {

        log.error("HANDLING >> Method argument not valid exception", exception);

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ExceptionResponse(HttpStatus.UNPROCESSABLE_ENTITY.toString()
                        , getFieldErrorMessage(exception.getBindingResult().getFieldErrors())
                        , exception.toString()));
    }

    @ExceptionHandler(GenericException.class)
    public ResponseEntity<ExceptionResponse> handleGenericException(GenericException exception) {
        log.error("HANDLING >> Generic Exception", exception);

        return ResponseEntity.status(exception.getStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ExceptionResponse(exception.getStatus().toString()
                        , getErrorSpecMessages(exception.getErrorInfo().getErrors())
                        , exception.toString()));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<ExceptionResponse> handleWebExchangeBindException(WebExchangeBindException exception) {
       log.error("HANDLING >> Web exchange method argument not valid exception", exception);

       return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
               .contentType(MediaType.APPLICATION_JSON)
               .body(new ExceptionResponse(HttpStatus.UNPROCESSABLE_ENTITY.toString(),
                       getFieldErrorMessage(exception.getBindingResult().getFieldErrors()),
                       exception.toString()));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ExceptionResponse> handleBusinessException(BusinessException exception) {
        log.error("HANDLING >> Business Exception", exception);

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ExceptionResponse(exception.getCode(), exception.getMessage(), exception.toString()));
    }

    @ExceptionHandler(NotPersistedException.class)
    public ResponseEntity<ExceptionResponse> handleNotPersistedException(BusinessException ex) {
        log.error(" HANDLING >> Not Persisted exception", ex);

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ExceptionResponse(ex.getCode(), ex.getMessage(), ex.toString()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionResponse> handleBadRequestException(BusinessException exception) {
        log.error("HANDLING >> Bad Request Exception", exception);

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ExceptionResponse(exception.getCode(), exception.getMessage(), exception.toString()));
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFoundException(ObjectNotFoundException exception) {
        log.error("HANDLING >> Not Found Exception", exception);

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ExceptionResponse(exception.getCode(), exception.getMessage(), exception.toString()));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ExceptionResponse> handleUnauthorizedException(UnauthorizedException exception) {
        log.error("HANDLING >> Unauthorized Exception");

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ExceptionResponse(exception.getCode(), exception.getMessage(), exception.toString()));
    }

    @ExceptionHandler(ProviderSSOException.class)
    public ResponseEntity<ExceptionResponse> handleProviderSSOException(ProviderSSOException exception) {
        log.error("HANDLING >> Provider SSO Exception", exception);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ExceptionResponse(HttpStatus.UNAUTHORIZED.toString(),
                        exception.getMessage(),
                        exception.toString()));
    }

    @ExceptionHandler(AuditException.class)
    public ResponseEntity<ExceptionResponse> handleAuditException(AuditException exception) {
        log.error("HANDLING >> Audit Exception", exception);

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ExceptionResponse(exception.getCode(), exception.getMessage(), exception.toString()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionResponse> handleDataIntegrityViolationException(
            DataIntegrityViolationException exception) {
        log.error("HANDLING >> Data Integrity Violation Exception", exception);

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ExceptionResponse(HttpStatus.CONFLICT.toString(),
                        exception.getMessage(),
                        exception.toString()));
    }

    @ExceptionHandler(SameIdentifierException.class)
    public ResponseEntity<ExceptionResponse> handleSameIdentifierException(SameIdentifierException exception) {
        log.error("HANDLING >> Same Identifier Exception", exception);

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ExceptionResponse(exception.getCode(), exception.getMessage(), exception.toString()));
    }

    @ExceptionHandler(StorageException.class)
    public ResponseEntity<ExceptionResponse> handleStorageException(StorageException exception) {
        log.error("HANDLING >> Storage Exception", exception);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                        exception.getMessage(),
                        exception.toString()));
    }

    private String getFieldErrorMessage(List<FieldError> fieldErrors) {
        List<String> messages = new ArrayList<>();
        fieldErrors.forEach(e -> messages.add("Campo '" + e.getField() + "': "
                + messageSource.getMessage(e, LocaleContextHolder.getLocale())));

        return String.join(";", messages);
    }

    private String getErrorSpecMessages(List<ErrorSpec> errorSpecs) {
        List<String> messages = new ArrayList<>();
        errorSpecs.forEach(e -> messages.add(e.getMessage()));

        return String.join("; ", messages);
    }
}
