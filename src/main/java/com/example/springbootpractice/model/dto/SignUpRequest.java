package com.example.springbootpractice.model.dto;

import com.example.springbootpractice.model.enums.Gender;
import com.example.springbootpractice.model.enums.Role;
import com.example.springbootpractice.model.enums.UserType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignUpRequest(
    @NotBlank
    String name,

    @NotBlank
    @Pattern(
        regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])+[.][a-zA-Z]{2,3}$",
        message = "올바르지 않은 이메일 형식 입니다."
    )
    String email,

    @NotBlank
    @Size(min = 10, max = 20)
    String password,

    @NotNull
    Role role,

    @NotNull
    Gender gender,

    @NotNull
    UserType type

) {

    @Override
    public String toString() {
        return "SignUpRequest{" +
            "name='" + name + '\'' +
            ", email='" + email + '\'' +
            ", password='" + password + '\'' +
            ", role=" + role +
            ", gender=" + gender +
            ", type=" + type +
            '}';
    }
}
