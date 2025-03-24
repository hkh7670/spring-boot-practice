package com.example.springbootpractice.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum WebtoonEvaluationType {
    LIKE("좋아요"),
    DISLIKE("싫어요");

    private final String desc;
}
