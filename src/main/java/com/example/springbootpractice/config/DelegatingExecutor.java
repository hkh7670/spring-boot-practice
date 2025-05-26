package com.example.springbootpractice.config;

import static com.example.springbootpractice.util.CommonConstants.THREAD_ID;

import java.util.UUID;
import java.util.concurrent.Executor;
import org.jboss.logging.MDC;

public class DelegatingExecutor implements Executor {

    private final Executor delegate;

    public DelegatingExecutor(Executor delegate) {
        this.delegate = delegate;
    }

    @Override
    public void execute(Runnable command) {
        String threadId = "async-thread-" + UUID.randomUUID();
        delegate.execute(() -> {
            MDC.put(THREAD_ID, threadId);
            try {
                command.run();
            } catch (Exception ignored) {

            } finally {
                MDC.remove(THREAD_ID);
            }
        });
    }
}