package com.example.springbootpractice.model.entity;

import com.example.springbootpractice.model.dto.WebtoonEvaluationRequest;
import com.example.springbootpractice.model.enums.WebtoonEvaluationType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "WEBTOON_EVALUATION_INFO",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "WEBTOON_EVALUATION_INFO_USER_SEQ_WEBTOON_SEQ_UNIQUE_KEY",
            columnNames = {"USER_SEQ", "WEBTOON_SEQ"}
        )
    })
@Comment("웹툰 작품 평가 정보 테이블")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class WebtoonEvaluationEntity extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long seq;

  @Column(name = "USER_SEQ", nullable = false)
  @Comment("유저 seq")
  private Long userSeq;

  @Column(name = "EVALUATION_TYPE", nullable = false)
  @Enumerated(EnumType.STRING)
  @Comment("작품 평가 타입")
  private WebtoonEvaluationType evaluationType;

  @Column(name = "COMMENT")
  @Comment("코멘트")
  private String comment;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "WEBTOON_SEQ")
  private WebtoonEntity webtoon;


  public static WebtoonEvaluationEntity of(WebtoonEntity webtoon, long userSeq,
      WebtoonEvaluationRequest request) {
    return WebtoonEvaluationEntity.builder()
        .userSeq(userSeq)
        .webtoon(webtoon)
        .evaluationType(request.evaluationType())
        .comment(request.comment())
        .build();
  }

}
