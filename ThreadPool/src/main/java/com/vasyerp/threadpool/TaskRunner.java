package com.vasyerp.threadpool;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TaskRunner implements CommandLineRunner {

    private final AsyncService asyncService;

    public TaskRunner(AsyncService asyncService) {
        this.asyncService = asyncService;
    }

    @Override
    public void run(String... args) {
        for (int i = 1; i <= 50; i++) {
            asyncService.processTask(i);
        }
    }
}
