package com.example.springbootpractice.client;

import java.util.Map;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface TMDBRestClient {

  @GetExchange(value = "/movie/popular")
  Map<String, Object> getPopularMovies(
      @RequestParam(name = "language", defaultValue = "ko-KR") String language,
      @RequestParam(name = "page", defaultValue = "1") Integer page
  );

}
