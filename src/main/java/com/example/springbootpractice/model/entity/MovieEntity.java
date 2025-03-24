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
@Table(name = "movie")
@Comment("영화 정보 테이블")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class MovieEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    @Comment("영화 제목")
    private String title;

    @Column(name = "overview", nullable = false, length = 500)
    @Comment("요약")
    private String overview;

    @Column(name = "original_title", nullable = false)
    @Comment("원 제목")
    private String originalTitle;

    @Column(name = "original_language", nullable = false)
    @Comment("언어")
    private String originalLanguage;

    @Column(name = "vote_count", nullable = false)
    private Long voteCount;

    @Column(name = "vote_average", nullable = false)
    private Double voteAverage;

    @Column(name = "popularity", nullable = false)
    private Double popularity;

    @Column(name = "poster_path", nullable = false)
    private String posterPath;

    @Column(name = "release_date", nullable = false)
    private LocalDate releaseDate;

    @Column(name = "video", nullable = false)
    private Boolean video;

    @Column(name = "adult", nullable = false)
    private Boolean adult;

    @Column(name = "backdrop_path", nullable = false)
    private String backdropPath;

    @Column(name = "page", nullable = false)
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
