package com.example.springbootpractice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorResponse {

  private final ErrorCode errorCode;
  private final String message;

  public ErrorResponse(ErrorCode errorCode) {
    this.errorCode = errorCode;
    this.message = errorCode.getMessage();
  }

}
