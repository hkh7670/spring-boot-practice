package com.example.springbootpractice.service;

import com.example.springbootpractice.client.TMDBRestClient;
import com.example.springbootpractice.model.dto.tmdb.PopularMoviePageResponse;
import com.example.springbootpractice.model.entity.MovieEntity;
import com.example.springbootpractice.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@Service
@RequiredArgsConstructor
public class TMDBService {

  private final UserService userService;
  private final TMDBRestClient tmdbRestClient;
  private final MovieRepository movieRepository;
  private final TMDB2Service tmdb2Service;

  public PopularMoviePageResponse getPopularMovies(int page) {
    return PopularMoviePageResponse.from(
        tmdbRestClient.getPopularMovies(null, page)
    );
  }

  @Transactional
  public void insertPopularMovies() {
    String transactionName = TransactionSynchronizationManager.getCurrentTransactionName();
    boolean isActualTransactionActive = TransactionSynchronizationManager.isActualTransactionActive();
    log.info("START TRANSACTION: {}", transactionName);
    log.info("isActualTransactionActive: {}", isActualTransactionActive);

    PopularMoviePageResponse res = getPopularMovies(1);
    movieRepository.saveAll(
        res.results().stream()
            .map(item -> MovieEntity.of(item, 1))
            .toList()
    );
    PopularMoviePageResponse res2 = getPopularMovies(2);
    tmdb2Service.insertPopularMovies2(res2);
//    try {
//      tmdb2Service.insertPopularMovies2(res2);
//    } catch (Exception e) {
//      log.info(e.getMessage());
//    }
//    throw new RuntimeException();

  }

}
