package com.example.springbootpractice.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "WEBTOON_VIEW_HISTORY_INFO")
@Comment("웹툰 조회 내역 정보 테이블")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class WebtoonViewHistoryEntity extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long seq;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "USER_SEQ")
  private UserEntity user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "WEBTOON_SEQ")
  private WebtoonEntity webtoon;

  public static WebtoonViewHistoryEntity of(UserEntity user, WebtoonEntity webtoon) {
    return WebtoonViewHistoryEntity.builder()
        .user(user)
        .webtoon(webtoon)
        .build();
  }

}
