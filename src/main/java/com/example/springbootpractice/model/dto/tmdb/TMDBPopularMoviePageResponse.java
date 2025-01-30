package com.example.springbootpractice.model.dto.tmdb;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record TMDBPopularMoviePageResponse(
    int page,
    List<TMDBPopularMovieInfo> results,
    @JsonProperty("total_pages")
    int totalPages,
    @JsonProperty("total_results")
    int totalResults
) {

  public record TMDBPopularMovieInfo(
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
      double popularity
  ) {

  }

}
