package ExecutorService.FutureInterface;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FutureExample {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        Callable<String> task = () -> {
            Thread.sleep(2000); // Simulate some work
            return "Task completed";
        };

        Future<String> future = executorService.submit(task);

        try {
            // Perform other operations while the task is running
            System.out.println("Doing other work...");

            // Retrieve the result of the task
            String result = future.get(); // This will block until the task is done
            System.out.println("Result: " + result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
    }
}