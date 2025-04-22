package com.example.springbootpractice.exception;

import lombok.AccessLevel;
import lombok.Builder;
import org.springframework.validation.FieldError;

@Builder(access = AccessLevel.PRIVATE)
public record ErrorField<T>(
    String fieldName, // 에러가 발생한 필드 명
    T fieldValue,     // 에러가 발생한 필드에 할당되어있는 값
    String message    // 에러 메시지
) {

    public static ErrorField<Object> from(FieldError fieldError) {
        return ErrorField.<Object>builder()
            .fieldName(fieldError.getField())
            .fieldValue(fieldError.getRejectedValue())
            .message(fieldError.getDefaultMessage())
            .build();
    }

}
