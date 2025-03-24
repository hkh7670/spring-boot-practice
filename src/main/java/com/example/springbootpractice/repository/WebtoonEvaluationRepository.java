package com.example.springbootpractice.repository;

import com.example.springbootpractice.model.entity.WebtoonEvaluationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WebtoonEvaluationRepository extends JpaRepository<WebtoonEvaluationEntity, Long>,
    WebtoonEvaluationCustomRepository {

  @Modifying
  @Query("delete from WebtoonEvaluationEntity we where we.userSeq = :userSeq")
  void deleteByUserSeq(long userSeq);

  boolean existsByUserSeqAndWebtoonSeq(long userSeq, long webtoonSeq);


}
