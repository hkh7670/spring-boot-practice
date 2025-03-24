package com.example.springbootpractice.model.dto;

import com.example.springbootpractice.model.entity.WebtoonEntity;
import com.example.springbootpractice.model.enums.WebtoonRatingType;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record WebtoonResponse(
    long seq,
    String name,
    String author,
    long coin,
    LocalDateTime openDate,
    WebtoonRatingType ratingType

) {

    public static WebtoonResponse from(WebtoonEntity entity) {
        return WebtoonResponse.builder()
            .seq(entity.getSeq())
            .name(entity.getName())
            .author(entity.getAuthor())
            .coin(entity.getCoin())
            .openDate(entity.getOpenDate())
            .ratingType(entity.getRatingType())
            .build();
    }

}
