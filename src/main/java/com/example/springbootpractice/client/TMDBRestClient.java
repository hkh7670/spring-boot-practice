package com.example.springbootpractice.client;

import com.example.springbootpractice.model.dto.tmdb.TMDBPopularMoviePageResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@Component
@HttpExchange
public interface TMDBRestClient {

    @GetExchange(value = "/movie/popular")
    TMDBPopularMoviePageResponse getPopularMovies(
        @RequestParam(name = "language", defaultValue = "ko-KR") String language,
        @RequestParam(name = "page", defaultValue = "1") Integer page
    );

}
