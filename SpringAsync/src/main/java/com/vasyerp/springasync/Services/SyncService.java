package com.vasyerp.springasync.Services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SyncService {
    private final Object lock = new Object();

    @Transactional
    public void processWithSynchronization() {
        synchronized (lock) {
            // Synchronized block implementation within a transaction
        }
    }
}