package com.vasyerp.threadpool;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncService {

    @Async("customExecutor")
    public void processTask(int taskId) {
        System.out.println(
                "Executing Task " + taskId +
                        " on thread " + Thread.currentThread().getName()
        );

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
