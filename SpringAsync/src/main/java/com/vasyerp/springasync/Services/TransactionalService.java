package com.vasyerp.springasync.Services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

@Service
public class TransactionalService {

    @Transactional
    public void performSynchronousTask() {
        // Synchronous task implementation within a transaction
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public CompletableFuture<String> performAsyncTask() {
        // Asynchronous task implementation within a new transaction
        return CompletableFuture.completedFuture("Async task completed!");
    }
}
