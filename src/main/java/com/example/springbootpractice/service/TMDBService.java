package com.example.springbootpractice.service;

import com.example.springbootpractice.client.TMDBRestClient;
import com.example.springbootpractice.config.AsyncConfig;
import com.example.springbootpractice.model.dto.tmdb.PopularMoviePageResponse;
import com.example.springbootpractice.model.entity.MovieEntity;
import com.example.springbootpractice.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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

    @Async(AsyncConfig.ASYNC_THREAD_EXECUTOR)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void insertPopularMovies(int page) {
        String transactionName = TransactionSynchronizationManager.getCurrentTransactionName();
        boolean isActualTransactionActive = TransactionSynchronizationManager.isActualTransactionActive();
        log.info("START TRANSACTION: {}", transactionName);
        log.info("isActualTransactionActive: {}", isActualTransactionActive);

        PopularMoviePageResponse res = getPopularMovies(page);
        movieRepository.saveAll(
            res.results().stream()
                .map(item -> MovieEntity.of(item, page))
                .toList()
        );
    }

}
