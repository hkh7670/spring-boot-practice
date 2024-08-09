package com.example.springbootpractice.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = WebtoonCommentValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface WebtoonComment {

  String message() default "웹툰 평가 코멘트는 특수문자를 허용하지 않습니다.";

  boolean nullable() default true;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}