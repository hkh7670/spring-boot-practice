package com.example.springbootpractice.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorResponse {

  private final ErrorCode errorCode;
  private final String message;
  @JsonInclude(Include.NON_EMPTY)
  private final List<ErrorField> errorFields;

  public ErrorResponse(ErrorCode errorCode) {
    this.errorCode = errorCode;
    this.message = errorCode.getMessage();
    this.errorFields = null;
  }

  public ErrorResponse(ErrorCode errorCode, List<ErrorField> errorFields) {
    this.errorCode = errorCode;
    this.message = errorCode.getMessage();
    this.errorFields = errorFields;
  }

}
