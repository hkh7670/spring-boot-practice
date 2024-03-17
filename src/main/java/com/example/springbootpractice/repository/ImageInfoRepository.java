package com.example.springbootpractice.repository;

import com.example.springbootpractice.model.entity.ImageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageInfoRepository extends JpaRepository<ImageInfo, Long> {

  @Query("select i from ImageInfo i where (:title is null or i.title like :title)")
  Page<ImageInfo> findAllByTitleLike(Pageable pageable, String title);
}
