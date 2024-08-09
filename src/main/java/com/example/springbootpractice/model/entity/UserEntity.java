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
@Table(name = "USER_INFO",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "USER_INFO_EMAIL_UNIQUE",
            columnNames = ("EMAIL")
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

  @Column(name = "NAME", nullable = false)
  @Comment("유저 이름")
  private String name;

  @Column(name = "EMAIL", nullable = false)
  @Comment("유저 이메일")
  private String email;

  @Column(name = "PASSWORD", nullable = false)
  @Comment("패스워드")
  private String password;

  @Column(name = "ROLE", nullable = false)
  @Enumerated(EnumType.STRING)
  private Role role;

  @Column(name = "GENDER", nullable = false)
  @Enumerated(EnumType.STRING)
  @Comment("성별")
  private Gender gender;

  @Column(name = "TYPE", nullable = false)
  @Enumerated(EnumType.STRING)
  @Comment("유형(일반 or 성인)")
  private UserType type;

  @Column(name = "ADULT_WEBTOON_VIEW_COUNT", nullable = false)
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

}
