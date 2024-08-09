package com.example.springbootpractice.model.dto;

import com.example.springbootpractice.model.enums.WebtoonEvaluationType;
import com.example.springbootpractice.validation.WebtoonComment;
import jakarta.validation.constraints.NotNull;

public record WebtoonEvaluationRequest(
    @NotNull
    WebtoonEvaluationType evaluationType,
    @WebtoonComment
    String comment

) {


}
