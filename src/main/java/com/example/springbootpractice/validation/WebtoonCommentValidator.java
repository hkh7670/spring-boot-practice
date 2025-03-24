package com.example.springbootpractice.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class WebtoonCommentValidator implements ConstraintValidator<WebtoonComment, String> {

    private WebtoonComment annotation;
    private static final String SPECIAL_CHARACTERS_PATTERN = "^[^@#$%^&*()\\-=_+\\[\\]{}|;:<>/\\\\]*$";

    @Override
    public void initialize(WebtoonComment constraintAnnotation) {
        this.annotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return this.annotation.nullable();
        }

        return value.matches(SPECIAL_CHARACTERS_PATTERN);
    }
}