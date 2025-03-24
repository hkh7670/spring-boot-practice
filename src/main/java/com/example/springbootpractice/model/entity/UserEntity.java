package com.example.springbootpractice.model.entity;

import com.example.springbootpractice.model.dto.SignUpRequest;
import com.example.springbootpractice.model.enums.Gender;
import com.example.springbootpractice.model.enums.Role;
import com.example.springbootpractice.model.enums.UserType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "user_info",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "user_info_email_unique",
            columnNames = ("email")
        )
    })
@Comment("유저 정보 테이블")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class UserEntity extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long seq;

  @Column(name = "name", nullable = false)
  @Comment("유저 이름")
  private String name;

  @Column(name = "email", nullable = false)
  @Comment("유저 이메일")
  private String email;

  @Column(name = "password", nullable = false)
  @Comment("패스워드")
  private String password;

  @Column(name = "role", nullable = false)
  @Enumerated(EnumType.STRING)
  private Role role;

  @Column(name = "gender", nullable = false)
  @Enumerated(EnumType.STRING)
  @Comment("성별")
  private Gender gender;

  @Column(name = "type", nullable = false)
  @Enumerated(EnumType.STRING)
  @Comment("유형(일반 or 성인)")
  private UserType type;

  @Column(name = "adult_webtoon_view_count", nullable = false)
  @ColumnDefault("0")
  @Comment("성인 작품 조회 수")
  private Long adultWebtoonViewCount;


  public static UserEntity of(SignUpRequest signUpRequest, String password) {
    return UserEntity.builder()
        .name(signUpRequest.name())
        .email(signUpRequest.email())
        .password(password)
        .role(signUpRequest.role())
        .gender(signUpRequest.gender())
        .type(signUpRequest.type())
        .adultWebtoonViewCount(0L)
        .build();
  }

  public void increaseAdultWebtoonViewCount() {
    this.adultWebtoonViewCount++;
  }

  // 웹툰 접근 권한이 있는지 체크
  public boolean canAccessWebtoon(WebtoonEntity webtoon) {
    // 성인 웹툰이 아닌 일반 웹툰이면 모두 접근 가능
    // 성인 웹툰인 경우, 성인만 접근 가능하다.
    return switch (webtoon.getRatingType()) {
      case NORMAL -> true;
      case ADULT -> this.type == UserType.ADULT;
    };
  }

  public boolean cannotAccessWebtoon(WebtoonEntity webtoon) {
    return !canAccessWebtoon(webtoon);
  }

}
