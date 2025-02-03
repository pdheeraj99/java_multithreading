package ExecutorService.SubmitvsExecute.Submit.Callable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PolicyReminderService {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Customer[] customers = {
                new Customer("Alice", "alice@example.com"),
                new Customer("Bob", "bob@example.com"),
                new Customer("Charlie", "charlie@example.com"),
                new Customer("David", "david@example.com"),
                new Customer("Eve", "eve@example.com")
        };

        ExecutorService executorService = Executors.newFixedThreadPool(3);

//---------------------------------WRONG
        // Here this one is wrong: Blocking s.get() within the loop:  The s.get() method blocks until the task completes. This effectively makes your loop sequential, negating the benefits of using an ExecutorService for parallel execution.  You're submitting tasks, but then immediately waiting for each one to finish before submitting the next.  This is not how you achieve concurrency.
//        for (Customer customer : customers) {
//            PolicyReminderTask task = new PolicyReminderTask(customer);
//            Future<String> s = executorService.submit(task);
//            System.out.println(s.get());
//        }

//
//        ---------------------------- RIGHT
        List<Future<String>> futures = new ArrayList<>();

        for (Customer customer : customers) {
            PolicyReminderTask task = new PolicyReminderTask(customer);
            Future<String> future = executorService.submit(task);
            futures.add(future); // Store the Futures
        }

        // Process the results asynchronously (outside the loop)
        for (Future<String> future : futures) {
            try {
                String result = future.get(); // Get the result, but do it after all tasks are submitted
                System.out.println(result);
            } catch (InterruptedException | ExecutionException e) {
                System.err.println("Error processing task: " + e.getMessage());
                // Handle the exception appropriately (e.g., log, retry, etc.)
                if (e instanceof ExecutionException) {
                    Throwable cause = e.getCause(); // Get the actual exception from the task
                    System.err.println("Cause of error: " + cause.getMessage());
                    cause.printStackTrace(); // Print stack trace of the actual exception
                }
            }
        }


        executorService.shutdown();
    }
}
