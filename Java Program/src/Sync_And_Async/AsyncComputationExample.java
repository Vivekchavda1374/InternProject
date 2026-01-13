package Sync_And_Async;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.LongStream;

public class AsyncComputationExample {

    public static void main(String[] args) {
//        // Creating a large list of numbers
//        List<Long> numbers = LongStream.rangeClosed(1, 1_000_000L).boxed().toList();
//
//        // Running the sum calculation asynchronously
//        CompletableFuture<Long> sumFuture = CompletableFuture.supplyAsync(() -> sumOfList(numbers));
//
//        // Do some other tasks if needed
//        System.out.println("Main thread is free to run other tasks.");
//
//        // Retrieve the result of the computation
//        try {
//            Long sum = sumFuture.get(); // This call is blocking, but the computation is already running asynchronously
//            System.out.println("Sum of numbers: " + sum);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        System.out.println("All tasks completed.");

//        Choosing the Right Method
//        Use thenCombine when we have two independent futures and want to do something with both results.
//        Use thenCompose when the execution of the second future depends on the result of the first one.
//
//        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> "Hello");
//        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> " World");
//        CompletableFuture<String> combinedFuture = future1.thenCombine(future2, (result1, result2) -> result1 + result2);
//
//        combinedFuture.thenAccept(System.out::println); // Prints "Hello World"

//        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> "100");
//        CompletableFuture<Integer> combinedFuture = future1.thenCompose(number -> CompletableFuture.supplyAsync(() -> Integer.parseInt(number) * 2));
//
//        combinedFuture.thenAccept(System.out::println); // Prints 200
//
//        Using CompletableFuture.allOf
//        CompletableFuture.allOf is used when we want to wait for all of the given CompletableFutures to complete. It takes an array of CompletableFutures and returns a new CompletableFuture<Void> that is achieved when all the given CompletableFutures are complete.

        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> "Task 1");
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> "Task 2");
        CompletableFuture<String> future3 = CompletableFuture.supplyAsync(() -> "Task 3");

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(future1, future2, future3);

        allFutures.thenRun(() -> {
            // This block will be executed after all the Futures are complete.
            System.out.println("All tasks are completed.");
        });
        allFutures.thenRun(() -> {
            try {
                System.out.println(future1.get());
                System.out.println(future2.get());
                System.out.println(future3.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
//
//        Using CompletableFuture.anyOf
//        In contrast, CompletableFuture.anyOf is used when we want to do something as soon as any of the given CompletableFutures completes. It also takes an array of CompletableFutures but returns a CompletableFuture<Object> that completes with the same result as the first of these CompletableFutures to complete.
//        CompletableFuture<Object> anyFuture = CompletableFuture.anyOf(future1, future2, future3);
//
//        anyFuture.thenAccept(result -> {
//            // This block will be executed after any of the Futures complete.
//            System.out.println("First completed task result: " + result);
//        });


//        Using exceptionally()
//        The exceptionally() method handles exceptions in a CompletableFuture pipeline. It takes a function called with the exception thrown from the future and returns a default value or another exception.

//        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
//            if (Math.random() > 0.5) {
//                throw new RuntimeException("Something went wrong");
//            }
//            return "Success";
//        });
//
//        CompletableFuture<String> exceptionallyHandledFuture = future.exceptionally(ex -> {
//            System.out.println("Error occurred: " + ex.getMessage());
//            return "Default Value";
//        });
//
//        exceptionallyHandledFuture.thenAccept(System.out::println);



//        Using handle()
//        The handle() method is more versatile than exceptionally(). It handles both the computation's result and any exception that might have been thrown. handle() takes a BiFunction with two arguments: the result and the exception.

//        CompletableFuture<String> futureWithHandle = future.handle((result, ex) -> {
//            if (ex != null) {
//                System.out.println("Error occurred: " + ex.getMessage());
//                return "Fallback Result";
//            } else {
//                return result;
//            }
//        });
//
//        futureWithHandle.thenAccept(System.out::println);

//        idDone()
//        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "Hello");
//// ... some operations ...
//        if (future.isDone()) {
//            // The future has completed but could be either successful, exceptional, or cancelled.
//        }

//        isCompletedExceptionally()
//        if (future.isCompletedExceptionally()) {
//            // The future has completed with an exception.
//        }



//        isCancelled()
//        if (future.isCancelled()) {
//            // The future was cancelled.
//        }

//        join()
//        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
//            // Simulating a long-running task
//            try {
//                TimeUnit.SECONDS.sleep(2);
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            }
//            return "Result of the computation";
//        });
//
//// Using join to retrieve the result. This will block until the future is complete.
//        String result = future.join();
//        System.out.println(result);







    }

    private static Long sumOfList(List<Long> numbers) {
        return numbers.stream().reduce(0L, Long::sum);
    }
}