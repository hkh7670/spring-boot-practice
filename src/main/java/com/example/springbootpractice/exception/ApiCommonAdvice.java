package com.example.springbootpractice.exception;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;

@Slf4j
@RestControllerAdvice
public class ApiCommonAdvice {

    @ExceptionHandler(ApiErrorException.class)
    public ResponseEntity<ErrorResponse<?>> handleApiErrorException(ApiErrorException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity
            .status(e.getErrorCode().getStatus())
            .body(
                createErrorResponse(e.getErrorCode())
            );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse<?> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException e
    ) {
        log.error(e.getMessage(), e);
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        return createErrorResponse(
            ErrorCode.SCHEMA_VALIDATE_ERROR,
            fieldErrors.stream()
                .map(ErrorField::from)
                .toList()
        );
    }

    // 외부 서버 Connection Timeout 처리를 위한 Handler
    @ExceptionHandler(ResourceAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse<?> handleResourceAccessException(ResourceAccessException e) {
        log.error(e.getMessage(), e);
        return createErrorResponse(ErrorCode.EXTERNAL_SERVER_TIMEOUT_ERROR);
    }

    @ExceptionHandler(RestClientException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse<?> handleRestClientException(RestClientException e) {
        log.error(e.getMessage(), e);
        return createErrorResponse(ErrorCode.EXTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse<?> handleException(Exception e) {
        log.error(e.getMessage(), e);
        return createErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    private ErrorResponse<?> createErrorResponse(ErrorCode errorCode) {
        ErrorResponse<?> errorResponse = ErrorResponse.from(errorCode);
        printErrorResponse(errorResponse);
        return errorResponse;
    }

    private <T> ErrorResponse<T> createErrorResponse(
        ErrorCode errorCode,
        List<ErrorField<T>> errorFields
    ) {
        ErrorResponse<T> errorResponse = ErrorResponse.of(errorCode, errorFields);
        printErrorResponse(errorResponse);
        return errorResponse;
    }

    private void printErrorResponse(ErrorResponse<?> errorResponse) {
        log.error("{} errorResponse: {}",
            errorResponse.errorCode().getStatus().value(),
            errorResponse
        );
    }
}
