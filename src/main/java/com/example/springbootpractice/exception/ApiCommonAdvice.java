package com.example.springbootpractice.exception;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ApiCommonAdvice {

    @ExceptionHandler(ApiErrorException.class)
    public ResponseEntity<ErrorResponse<?>> handleApiErrorException(ApiErrorException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity
            .status(e.getErrorCode().getStatus())
            .body(new ErrorResponse<>(e.getErrorCode()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse<?> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException e
    ) {
        log.error(e.getMessage(), e);
        List<FieldError> errorFields = e.getBindingResult().getFieldErrors();
        return new ErrorResponse<>(
            ErrorCode.SCHEMA_VALIDATE_ERROR,
            errorFields.stream()
                .map(item -> ErrorField.of(
                    item.getField(),
                    item.getRejectedValue(),
                    item.getDefaultMessage()
                ))
                .toList()
        );
    }

    @ExceptionHandler(RestClientException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse<?> handleRestClientException(RestClientException e) {
        log.error(e.getMessage(), e);
        return new ErrorResponse<>(ErrorCode.EXTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse<?> handleException(Exception e) {
        log.error(e.getMessage(), e);
        return new ErrorResponse<>(ErrorCode.INTERNAL_SERVER_ERROR);
    }
}
