package com.example.springbootpractice.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum WebtoonRatingType {
  NORMAL("일반"),
  ADULT("성인");

  private final String desc;
}
