package com.example.springbootpractice.exception;

import java.io.Serial;
import lombok.Getter;

@Getter
public class ApiErrorException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;


    private final ErrorCode errorCode;

    public ApiErrorException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
