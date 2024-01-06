package com.example.application.exception.util;

import com.example.application.exception.constants.ExceptionConstants;
import com.example.application.exception.errorType.GenericException;
import com.example.application.exception.response.ErrorInfo;
import com.example.application.exception.response.ErrorSpec;
import com.example.application.exception.response.Issue;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionUtils {

    public static GenericException buildGenericException(@NonNull HttpStatus status,
                                                         String message,
                                                         String suggestedUserAction,
                                                         String suggestedApplicationAction) {

        var userActionSuggestionMessage = MessageResource.getInstance()
                .getMessage(ExceptionConstants.MessageConstants.SUGGESTED_ACTION_USER);

        var applicationActionSuggestionMessage = MessageResource.getInstance()
                .getMessage(ExceptionConstants.MessageConstants.SUGGESTED_ACTION_APPLICATION);

        var msg = MessageResource.getInstance()
                .getMessage(message);

        return GenericException.builder()
                .status(status)
                .errorInfo(ErrorInfo.builder()
                        .errors(List.of(ErrorSpec.builder()
                                .suggestedUserActions(List.of(Objects.isNull(suggestedUserAction) ? userActionSuggestionMessage : suggestedUserAction))
                                .suggestedApplicationActions(List.of(Objects.isNull(suggestedApplicationAction) ? applicationActionSuggestionMessage : suggestedApplicationAction))
                                .name(status.name())
                                .message(msg)
                                .issues(List.of(Issue.builder()
                                        .issueText(msg)
                                        .build()))
                                .httpStatusCodes(List.of(status))
                                .build()))
                        .build())
                .build();
    }

    public static GenericException buildBadRequestException(String message) {
        return buildGenericException(HttpStatus.BAD_REQUEST, message, null, null);
    }

    public static GenericException buildNotFoundException(String message) {
        return buildGenericException(HttpStatus.BAD_REQUEST, message, null, null);
    }

    public static GenericException buildNotPersistedException(String message) {
        return buildGenericException(HttpStatus.BAD_REQUEST, message, null, null);
    }

    public static GenericException buildSameIdentifierException(String message) {
        return buildGenericException(HttpStatus.BAD_REQUEST, message, null, null);
    }
}
