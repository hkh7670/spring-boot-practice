package com.example.springbootpractice.model.dto.tmdb;

import com.example.springbootpractice.model.dto.tmdb.TMDBPopularMoviePageResponse.TMDBPopularMovieInfo;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record PopularMoviePageResponse(
    int page,
    List<PopularMovieInfo> results,
    int totalPages,
    int totalResults
) {

  @Builder(access = AccessLevel.PRIVATE)
  public record PopularMovieInfo(
      String title,
      String overview,
      String originalTitle,
      String originalLanguage,
      int voteCount,
      double voteAverage,
      double popularity
  ) {

    public static PopularMovieInfo from(TMDBPopularMovieInfo tmdbPopularMovieInfo) {
      return PopularMovieInfo.builder()
          .title(tmdbPopularMovieInfo.title())
          .overview(tmdbPopularMovieInfo.overview())
          .originalTitle(tmdbPopularMovieInfo.originalTitle())
          .originalLanguage(tmdbPopularMovieInfo.originalLanguage())
          .voteCount(tmdbPopularMovieInfo.voteCount())
          .voteAverage(tmdbPopularMovieInfo.voteAverage())
          .popularity(tmdbPopularMovieInfo.popularity())
          .build();
    }

  }

  public static PopularMoviePageResponse from(TMDBPopularMoviePageResponse response) {
    return PopularMoviePageResponse.builder()
        .page(response.page())
        .totalPages(response.totalPages())
        .totalResults(response.totalResults())
        .results(response.results().stream()
            .map(PopularMovieInfo::from)
            .toList()
        )
        .build();
  }

}
