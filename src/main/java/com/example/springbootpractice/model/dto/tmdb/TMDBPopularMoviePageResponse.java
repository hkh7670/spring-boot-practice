package com.example.springbootpractice.model.dto.tmdb;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.util.List;

public record TMDBPopularMoviePageResponse(
    int page,
    @JsonProperty("total_pages")
    int totalPages,
    @JsonProperty("total_results")
    int totalResults,
    List<TMDBPopularMovieInfo> results
) {

  public record TMDBPopularMovieInfo(
      long id,
      
      String title,

      String overview,

      @JsonProperty("original_title")
      String originalTitle,

      @JsonProperty("original_language")
      String originalLanguage,

      @JsonProperty("vote_count")
      int voteCount,

      @JsonProperty("vote_average")
      double voteAverage,

      double popularity,

      @JsonProperty("poster_path")
      String posterPath,

      @JsonProperty("release_date")
      LocalDate releaseDate,

      boolean video,

      boolean adult,

      @JsonProperty("backdrop_path")
      String backdropPath,

      @JsonProperty("genre_ids")
      List<Long> genreIds
  ) {

  }

}
