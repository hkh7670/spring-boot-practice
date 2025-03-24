package com.example.springbootpractice.model.dto.tmdb;

import com.example.springbootpractice.model.dto.tmdb.TMDBPopularMoviePageResponse.TMDBPopularMovieInfo;
import java.time.LocalDate;
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
        long id,
        String title,
        String overview,
        String originalTitle,
        String originalLanguage,
        long voteCount,
        double voteAverage,
        double popularity,
        String posterPath,
        LocalDate releaseDate,
        boolean video,
        boolean adult,
        String backdropPath,
        List<Long> genreIds
    ) {

        public static PopularMovieInfo from(TMDBPopularMovieInfo tmdbPopularMovieInfo) {
            return PopularMovieInfo.builder()
                .id(tmdbPopularMovieInfo.id())
                .title(tmdbPopularMovieInfo.title())
                .overview(tmdbPopularMovieInfo.overview())
                .originalTitle(tmdbPopularMovieInfo.originalTitle())
                .originalLanguage(tmdbPopularMovieInfo.originalLanguage())
                .voteCount(tmdbPopularMovieInfo.voteCount())
                .voteAverage(tmdbPopularMovieInfo.voteAverage())
                .popularity(tmdbPopularMovieInfo.popularity())
                .posterPath(tmdbPopularMovieInfo.posterPath())
                .releaseDate(tmdbPopularMovieInfo.releaseDate())
                .video(tmdbPopularMovieInfo.video())
                .adult(tmdbPopularMovieInfo.adult())
                .backdropPath(tmdbPopularMovieInfo.backdropPath())
                .genreIds(tmdbPopularMovieInfo.genreIds())
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
