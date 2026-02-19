package Sync_And_Async;

import java.util.concurrent.CompletableFuture;

public class AsynchronousProgramming {
    static CompletableFuture<String> fetchDataAsync() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Data from Remote Service";
        });
    }
    static CompletableFuture<String> processDataAsync(String data) {
        return CompletableFuture.supplyAsync(() -> {
            return STR."Processed: \{data.toUpperCase()}";
        });
    }

    public static void main(String[] args) {
//        CompletableFuture.runAsync(() -> {
//            System.out.println("Task 1");
//        });
//
//        CompletableFuture.runAsync(() -> {
//            System.out.println("Task 2");
//        });
//
//        CompletableFuture.runAsync(() -> {
//            System.out.println("Task 3");
//        });

        CompletableFuture<String> fetchDataFuture = fetchDataAsync();
        CompletableFuture<String> processedDataFuture = fetchDataFuture.thenCompose(AsynchronousProgramming::processDataAsync);
        CompletableFuture<Void> combinedFuture = processedDataFuture.thenAccept(combinedResult -> {
            System.out.println(STR."Combined Result:\{combinedResult}");
        });
        combinedFuture.join();
    }
}
