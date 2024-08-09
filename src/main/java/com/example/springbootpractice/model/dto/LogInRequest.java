package com.example.springbootpractice.model.dto;

import jakarta.validation.constraints.NotBlank;

public record LogInRequest(
    @NotBlank
    String email,
    @NotBlank
    String password

) {


}
