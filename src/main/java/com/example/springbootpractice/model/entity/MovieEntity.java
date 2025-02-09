package com.example.springbootpractice.model.entity;

import com.example.springbootpractice.model.dto.tmdb.PopularMoviePageResponse.PopularMovieInfo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Table(name = "MOVIE")
@Comment("영화 정보 테이블")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class MovieEntity extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "TITLE", nullable = false)
  @Comment("영화 제목")
  private String title;

  @Column(name = "OVERVIEW", nullable = false, length = 500)
  @Comment("요약")
  private String overview;

  @Column(name = "ORIGINAL_TITLE", nullable = false)
  @Comment("원 제목")
  private String originalTitle;

  @Column(name = "ORIGINAL_LANGUAGE", nullable = false)
  @Comment("언어")
  private String originalLanguage;

  @Column(name = "VOTE_COUNT", nullable = false)
  private Long voteCount;

  @Column(name = "VOTE_AVERAGE", nullable = false)
  private Double voteAverage;

  @Column(name = "POPULARITY", nullable = false)
  private Double popularity;

  @Column(name = "POSTER_PATH", nullable = false)
  private String posterPath;

  @Column(name = "RELEASE_DATE", nullable = false)
  private LocalDate releaseDate;

  @Column(name = "VIDEO", nullable = false)
  private Boolean video;

  @Column(name = "ADULT", nullable = false)
  private Boolean adult;

  @Column(name = "BACKDROP_PATH", nullable = false)
  private String backdropPath;

  @Column(name = "PAGE", nullable = false)
  private long page;

  public static MovieEntity from(PopularMovieInfo info) {
    return MovieEntity.builder()
        .title(info.title())
        .overview(info.overview())
        .originalTitle(info.originalTitle())
        .originalLanguage(info.originalLanguage())
        .voteCount(info.voteCount())
        .voteAverage(info.voteAverage())
        .popularity(info.popularity())
        .posterPath(info.posterPath())
        .releaseDate(info.releaseDate())
        .video(info.video())
        .adult(info.adult())
        .backdropPath(info.backdropPath())
        .build();
  }

  public static MovieEntity of(PopularMovieInfo info, long page) {
    return MovieEntity.builder()
        .title(info.title())
        .overview(info.overview())
        .originalTitle(info.originalTitle())
        .originalLanguage(info.originalLanguage())
        .voteCount(info.voteCount())
        .voteAverage(info.voteAverage())
        .popularity(info.popularity())
        .posterPath(info.posterPath())
        .releaseDate(info.releaseDate())
        .video(info.video())
        .adult(info.adult())
        .backdropPath(info.backdropPath())
        .page(page)
        .build();
  }

}
