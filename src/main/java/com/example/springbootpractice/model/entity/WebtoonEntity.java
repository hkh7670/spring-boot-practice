package com.example.springbootpractice.model.entity;

import com.example.springbootpractice.model.enums.WebtoonRatingType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "webtoon_info")
@Comment("웹툰 작품 정보 테이블")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class WebtoonEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(name = "name", nullable = false)
    @Comment("작품명")
    private String name;

    @Column(name = "author", nullable = false)
    @Comment("작가")
    private String author;

    @Column(name = "coin", nullable = false)
    @Comment("금액")
    private Long coin;

    @Column(name = "open_date", nullable = false, columnDefinition = "TIMESTAMP(6)")
    @Comment("서비스 제공일")
    private LocalDateTime openDate;

    @Column(name = "rating_type", nullable = false)
    @Enumerated(EnumType.STRING)
    @Comment("웹툰 등급 (일반 or 성인물)")
    private WebtoonRatingType ratingType;

    public void updateCoin(Long coin) {
        this.coin = coin;
    }
}
