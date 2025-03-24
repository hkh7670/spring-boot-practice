package com.example.springbootpractice.repository;

import com.example.springbootpractice.model.dto.AdultWebtoonViewersResponse;
import com.example.springbootpractice.model.entity.QUserEntity;
import com.example.springbootpractice.model.entity.QWebtoonEntity;
import com.example.springbootpractice.model.entity.QWebtoonViewHistoryEntity;
import com.example.springbootpractice.model.enums.WebtoonRatingType;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class UserCustomRepositoryImpl implements UserCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<AdultWebtoonViewersResponse> findAdultWebtoonViewers(LocalDateTime from,
        LocalDateTime to, Pageable pageable) {
        QUserEntity user = QUserEntity.userEntity;
        QWebtoonViewHistoryEntity viewHistory = QWebtoonViewHistoryEntity.webtoonViewHistoryEntity;
        QWebtoonEntity webtoon = QWebtoonEntity.webtoonEntity;
        List<AdultWebtoonViewersResponse> result = jpaQueryFactory
            .select(
                Projections.constructor(AdultWebtoonViewersResponse.class,
                    user.seq,
                    user.name,
                    user.email,
                    user.gender,
                    user.type,
                    viewHistory.count()
                )
            )
            .from(viewHistory)
            .innerJoin(viewHistory.user, user)
            .innerJoin(viewHistory.webtoon, webtoon)
            .where(webtoon.ratingType.eq(WebtoonRatingType.ADULT),
                user.regDate.between(from, to))
            .groupBy(user.seq, user.name, user.email, user.gender, user.type)
            .having(viewHistory.count().goe(3))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        long totalCount = Objects.requireNonNullElse(
            jpaQueryFactory
                .select(
                    user.count()
                )
                .from(viewHistory)
                .innerJoin(viewHistory.user, user)
                .innerJoin(viewHistory.webtoon, webtoon)
                .where(webtoon.ratingType.eq(WebtoonRatingType.ADULT),
                    user.regDate.between(from, to))
                .groupBy(user.seq)
                .having(viewHistory.count().goe(3))
                .fetchOne(),
            0L
        );

        return new PageImpl<>(
            result,
            pageable,
            totalCount
        );
    }
}
