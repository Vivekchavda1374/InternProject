package com.vasyerp.springbootproject.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class AsyncServiceTest {

    @Autowired
    private ProductSrvcs productSrvcs;

    @Test
    public void testAsyncMethod() throws ExecutionException, InterruptedException {
        System.out.println("[DEBUG_LOG] Main thread: " + Thread.currentThread().getName());
        
        CompletableFuture<Void> future = productSrvcs.performAsyncTask();
        
        // The async method should return immediately with a future.
        // We wait for it to complete to verify it actually ran.
        future.get(); 
        
        assertTrue(future.isDone());
        System.out.println("[DEBUG_LOG] Test completed");
    }
}
