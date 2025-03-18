package com.example.springbootpractice.service;

import com.example.springbootpractice.model.dto.tmdb.PopularMoviePageResponse;
import com.example.springbootpractice.model.entity.MovieEntity;
import com.example.springbootpractice.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@Service
@RequiredArgsConstructor
public class TMDB2Service {

    private final MovieRepository movieRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void insertPopularMovies2(PopularMoviePageResponse res) {
        String transactionName = TransactionSynchronizationManager.getCurrentTransactionName();
        boolean isActualTransactionActive = TransactionSynchronizationManager.isActualTransactionActive();
        log.info("START TRANSACTION: {}", transactionName);
        log.info("isActualTransactionActive: {}", isActualTransactionActive);

        movieRepository.saveAll(
            res.results().stream()
                .map(item -> MovieEntity.of(item, 2))
                .toList()
        );
    throw new RuntimeException();
    }


}
