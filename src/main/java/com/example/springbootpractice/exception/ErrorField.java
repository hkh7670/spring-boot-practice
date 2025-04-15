package com.example.springbootpractice.exception;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record ErrorField<T>(
    String fieldName, // 에러가 발생한 필드 명
    T fieldValue,     // 에러가 발생한 필드에 할당되어있는 값
    String message    // 에러 메시지
) {

    public static <T> ErrorField<T> of(
        String fieldName,
        T fieldValue,
        String message
    ) {
        return ErrorField.<T>builder()
            .fieldName(fieldName)
            .fieldValue(fieldValue)
            .message(message)
            .build();
    }

}
