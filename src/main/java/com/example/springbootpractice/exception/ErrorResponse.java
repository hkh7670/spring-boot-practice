package com.example.springbootpractice.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;

public record ErrorResponse<T>(
    ErrorCode errorCode,

    String message,

    @JsonInclude(Include.NON_EMPTY)
    List<ErrorField<T>> errorFields
) {

    public static ErrorResponse<?> from(ErrorCode errorCode) {
        return new ErrorResponse<>(
            errorCode,
            errorCode.getMessage(),
            null
        );
    }

    public static <T> ErrorResponse<T> of(
        ErrorCode errorCode,
        List<ErrorField<T>> errorFields
    ) {
        return new ErrorResponse<>(
            errorCode,
            errorCode.getMessage(),
            errorFields
        );
    }
}
