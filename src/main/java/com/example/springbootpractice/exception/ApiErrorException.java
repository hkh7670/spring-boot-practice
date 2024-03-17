package com.example.springbootpractice.exception;

import lombok.Getter;

@Getter
public class ApiErrorException extends RuntimeException {

  private final ErrorCode errorCode;

  public ApiErrorException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }

}
