package com.example.springbootpractice.config;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@Configuration
public class AsyncConfig {

    public static final String ASYNC_THREAD_EXECUTOR = "asyncThreadExecutor";

    @Bean(name = ASYNC_THREAD_EXECUTOR)
    public Executor asyncThreadExecutor() {
        return new DelegatingExecutor(Executors.newVirtualThreadPerTaskExecutor());
    }
}
