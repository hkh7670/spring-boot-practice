package com.example.springbootpractice.repository;

import com.example.springbootpractice.model.entity.WebtoonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebtoonRepository extends JpaRepository<WebtoonEntity, Long> {

  boolean existsBySeq(long seq);

}
