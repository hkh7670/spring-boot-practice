package com.example.springbootpractice.repository;

import com.example.springbootpractice.model.entity.UserEntity;
import com.example.springbootpractice.model.entity.WebtoonEntity;
import com.example.springbootpractice.model.entity.WebtoonViewHistoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WebtoonViewHistoryRepository extends
    JpaRepository<WebtoonViewHistoryEntity, Long> {

    @Modifying
    @Query("delete from WebtoonViewHistoryEntity wvh where wvh.user = :user")
    void deleteByUser(UserEntity user);

    @Query("select wvh from WebtoonViewHistoryEntity wvh "
        + "join fetch wvh.user "
        + "where wvh.webtoon = :webtoon ")
    Page<WebtoonViewHistoryEntity> findWebtoonViewHistory(@Param("webtoon") WebtoonEntity webtoon,
        Pageable pageable);


}
