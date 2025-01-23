package com.example.springbootpractice.controller;

import com.example.springbootpractice.client.TMDBRestClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tmdb")
public class TMDBController {

  private final TMDBRestClient tmdbRestClient;

  /**
   * 인기 영화 조회
   * @return 인기 영화 리스트
   */
  @GetMapping
  public Object requestSignUp() {
    var data = tmdbRestClient.getPopularMovies(null, null);
    return data;
  }


}
