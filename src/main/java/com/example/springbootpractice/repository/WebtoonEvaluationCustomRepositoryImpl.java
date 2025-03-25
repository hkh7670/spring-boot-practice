package com.example.springbootpractice.repository;

import com.example.springbootpractice.model.dto.WebtoonTop3Info;
import com.example.springbootpractice.model.entity.QWebtoonEntity;
import com.example.springbootpractice.model.entity.QWebtoonEvaluationEntity;
import com.example.springbootpractice.model.enums.WebtoonEvaluationType;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WebtoonEvaluationCustomRepositoryImpl implements WebtoonEvaluationCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<WebtoonTop3Info> findTop3Webtoons(WebtoonEvaluationType evaluationType) {
        QWebtoonEvaluationEntity webtoonEvaluation = QWebtoonEvaluationEntity.webtoonEvaluationEntity;
        QWebtoonEntity webtoon = QWebtoonEntity.webtoonEntity;
        return this.jpaQueryFactory
            .select(
                Projections.constructor(WebtoonTop3Info.class,
                    webtoon.seq,
                    webtoonEvaluation.count(),
                    webtoon.name,
                    webtoon.author,
                    webtoon.ratingType,
                    webtoon.coin,
                    webtoon.openDate
                )
            )
            .from(webtoonEvaluation)
            .innerJoin(
                webtoonEvaluation.webtoon,
                webtoon
            )
            .where(webtoonEvaluation.evaluationType.eq(evaluationType))
            .groupBy(
                webtoon.seq,
                webtoon.name,
                webtoon.author,
                webtoon.ratingType,
                webtoon.coin,
                webtoon.openDate
            )
            .orderBy(
                webtoonEvaluation.count().desc(),
                webtoon.name.asc()
            )
            .limit(3)
            .fetch();
    }
}
