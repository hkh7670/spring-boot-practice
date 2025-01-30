package com.example.springbootpractice.controller;

import com.example.springbootpractice.model.dto.tmdb.PopularMoviePageResponse;
import com.example.springbootpractice.service.TMDBService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tmdb")
public class TMDBController {

  private final TMDBService tmdbService;

  /**
   * 인기 영화 조회
   *
   * @return 인기 영화 리스트
   */
  @GetMapping("/movie/popular")
  public PopularMoviePageResponse requestSignUp(
      @RequestParam(required = false, defaultValue = "1") int page) {
    return tmdbService.getPopularMovies(page);
  }


}
