package com.vasyerp.springasync.Services;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

@Service
public class TaskService {

    @Async
    public CompletableFuture<String> performAsyncTask() {
        // Asynchronous task implementation
        return CompletableFuture.completedFuture("Async task completed!");
    }

    @Transactional
    public String performSynchronousTask() {
        // Synchronous task implementation
        return "Synchronous task completed!";
    }
}