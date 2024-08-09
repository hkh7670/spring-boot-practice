package com.example.springbootpractice.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateWebtoonCoinRequest(
    @NotNull
    @Min(0)
    @Max(500)
    Long coin
) {


}
