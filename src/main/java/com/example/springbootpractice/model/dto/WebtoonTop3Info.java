package com.example.springbootpractice.model.dto;

import com.example.springbootpractice.model.enums.WebtoonRatingType;
import java.time.LocalDateTime;

public record WebtoonTop3Info(
    Long webtoonSeq,
    Long count,
    String name,
    String author,
    WebtoonRatingType ratingType,
    Long coin,
    LocalDateTime openDate
) {


}
