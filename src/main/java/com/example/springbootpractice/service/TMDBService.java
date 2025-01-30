package com.example.springbootpractice.service;

import com.example.springbootpractice.client.TMDBRestClient;
import com.example.springbootpractice.model.dto.tmdb.PopularMoviePageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TMDBService {

  private final UserService userService;
  private final TMDBRestClient tmdbRestClient;

  public PopularMoviePageResponse getPopularMovies(int page) {
    return PopularMoviePageResponse.from(
        tmdbRestClient.getPopularMovies(null, page)
    );
  }

  

}
