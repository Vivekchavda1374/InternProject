package com.vasyerp.springasync.Controller;

import com.vasyerp.springasync.Services.AsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
public class AsyncController {
    @Autowired
    private AsyncService asyncService;

    @GetMapping("/async-task")
    public ResponseEntity<String> triggerAsyncTask() {
        CompletableFuture<String> asyncResult = asyncService.performAsyncTask();
        return ResponseEntity.ok("Async task triggered. Result: " + asyncResult.join());
    }
}
