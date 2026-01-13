package Sync_And_Async;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.io.IOException;

public class AsyncFileWritingExample {

    public static void main(String[] args) {
        List<String> dataToWrite = List.of("Line 1", "Line 2", "Line 3");

        CompletableFuture<Void> fileWritingFuture = CompletableFuture.runAsync(() -> {
            try {
                writeFile("example.txt", dataToWrite);
                System.out.println("File writing completed asynchronously: " + Thread.currentThread().getName());
            } catch (IOException e) {
                System.err.println("Error occurred while writing to the file: " + e.getMessage());
            }
        });

        // Continue with other operations
        System.out.println("Main thread is free to run other tasks.");

        // Optionally, wait for the file writing to complete
        try {
            fileWritingFuture.get(); // Blocks until the asynchronous file writing is complete
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("All tasks completed.");
    }

    private static void writeFile(String fileName, List<String> data) throws IOException {
        Path path = Path.of(fileName);
        Files.write(path, data, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }
}
