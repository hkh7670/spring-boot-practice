package com.example.springbootpractice.repository;

import com.example.springbootpractice.model.entity.UserEntity;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmail(String email);

    @Modifying
    @Query("delete from UserEntity u where u.seq = :userSeq")
    void deleteByUserSeq(long userSeq);

    @Query("select u from UserEntity u "
        + "where u.adultWebtoonViewCount >=3 "
        + "and u.regDate between :from and :to ")
    Page<UserEntity> findAdultWebtoonViewers(LocalDateTime from, LocalDateTime to,
        Pageable pageable);

}
