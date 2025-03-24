package com.example.springbootpractice.model.dto;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record SignUpResponse(
    long seq

) {

    public static SignUpResponse of(long seq) {
        return SignUpResponse.builder()
            .seq(seq)
            .build();
    }

}
