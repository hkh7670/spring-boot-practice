package com.example.springbootpractice.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ErrorResponse<T> {

    private final ErrorCode errorCode;
    private final String message;

    @JsonInclude(Include.NON_EMPTY)
    private final List<ErrorField<T>> errorFields;

    private ErrorResponse(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
        this.errorFields = null;
    }

    private ErrorResponse(
        ErrorCode errorCode,
        List<ErrorField<T>> errorFields
    ) {
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
        this.errorFields = errorFields;
    }

    public static ErrorResponse<?> from(ErrorCode errorCode) {
        return new ErrorResponse<>(errorCode);
    }

    public static <T> ErrorResponse<T> of(
        ErrorCode errorCode,
        List<ErrorField<T>> errorFields
    ) {
        return new ErrorResponse<>(errorCode, errorFields);
    }

}
