package com.example.springbootpractice.model.dto;

import com.example.springbootpractice.model.enums.WebtoonRatingType;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record WebtoonTop3Response(
    List<WebtoonCountInfoResponse> likeTop3List,
    List<WebtoonCountInfoResponse> dislikeTop3List
) {


  @Builder(access = AccessLevel.PRIVATE)
  private record WebtoonCountInfoResponse(
      Long seq,
      Long count,
      String name,
      String author,
      Long coin,
      LocalDateTime openDate,
      WebtoonRatingType ratingType
  ) {

  }

  public static WebtoonTop3Response of(
      List<WebtoonTop3Info> likeTop3Info,
      List<WebtoonTop3Info> dislikeTop3Info
  ) {
    return WebtoonTop3Response.builder()
        .likeTop3List(likeTop3Info.stream()
            .map(item -> WebtoonCountInfoResponse.builder()
                .seq(item.webtoonSeq())
                .count(item.count())
                .name(item.name())
                .author(item.author())
                .coin(item.coin())
                .openDate(item.openDate())
                .ratingType(item.ratingType())
                .build())
            .toList())
        .dislikeTop3List(dislikeTop3Info.stream()
            .map(item -> WebtoonCountInfoResponse.builder()
                .seq(item.webtoonSeq())
                .count(item.count())
                .name(item.name())
                .author(item.author())
                .coin(item.coin())
                .openDate(item.openDate())
                .ratingType(item.ratingType())
                .build())
            .toList())
        .build();
  }


}
