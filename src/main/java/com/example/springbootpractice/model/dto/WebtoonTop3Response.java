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

        public static WebtoonCountInfoResponse from(WebtoonTop3Info top3Info) {
            return WebtoonCountInfoResponse
                .builder()
                .seq(top3Info.webtoonSeq())
                .count(top3Info.count())
                .name(top3Info.name())
                .author(top3Info.author())
                .coin(top3Info.coin())
                .openDate(top3Info.openDate())
                .ratingType(top3Info.ratingType())
                .build();
        }

    }

    public static WebtoonTop3Response of(
        List<WebtoonTop3Info> likeTop3Info,
        List<WebtoonTop3Info> dislikeTop3Info
    ) {
        return WebtoonTop3Response.builder()
            .likeTop3List(
                likeTop3Info.stream()
                    .map(WebtoonCountInfoResponse::from)
                    .toList()
            )
            .dislikeTop3List(
                dislikeTop3Info.stream()
                    .map(WebtoonCountInfoResponse::from)
                    .toList()
            )
            .build();
    }


}
