package com.vasyerp.springasync.Services;

import jakarta.persistence.criteria.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

@Service
public class OrderService {
    @Autowired
    private PaymentService paymentService;
    @Async
    public CompletableFuture<String> processOrderAsync(Order order) {
        // Async processing, e.g., inventory update, notification to user
        return CompletableFuture.completedFuture("Order processed asynchronously!");
    }
    @Transactional
    public String processOrderSync(Order order) {
        // Synchronous processing, e.g., payment processing
        paymentService.processPayment(order);
        return "Order processed synchronously!";
    }
}
