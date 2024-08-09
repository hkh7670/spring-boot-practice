package com.example.springbootpractice.exception;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record ErrorField(
    String fieldName,
    String message
) {

  public static ErrorField of(String fieldName, String message) {
    return ErrorField.builder()
        .fieldName(fieldName)
        .message(message)
        .build();
  }

}
