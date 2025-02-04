package ExecutorService.ScheduledThreadPool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;

public class PolicyReminderService {
    public static void main(String[] args) {
        Customer[] customers = {
                new Customer("Alice", "alice@example.com"),
                new Customer("Bob", "bob@example.com"),
                new Customer("Charlie", "charlie@example.com"),
                new Customer("David", "david@example.com"),
                new Customer("Eve", "eve@example.com")
        };

        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(3);

        // List to store Future objects for Callable tasks
        List<ScheduledFuture<String>> futures = new ArrayList<>();

        // Schedule Callable tasks with a delay
        for (Customer customer : customers) {
            PolicyReminderCallableTask callableTask = new PolicyReminderCallableTask(customer);
            ScheduledFuture<String> future = scheduledExecutorService.schedule(callableTask, 5, TimeUnit.SECONDS);
            futures.add(future);
        }

        // Schedule Runnable tasks at a fixed rate
        for (Customer customer : customers) {
            PolicyReminderRunnableTask runnableTask = new PolicyReminderRunnableTask(customer);
            scheduledExecutorService.scheduleAtFixedRate(runnableTask, 0, 10, TimeUnit.SECONDS);
        }

        // Schedule a periodic Runnable task with a fixed delay
        scheduledExecutorService.scheduleWithFixedDelay(() -> {
            System.out.println("Running periodic task with fixed delay");
        }, 0, 15, TimeUnit.SECONDS);

        // Check results of Callable tasks
        for (Future<String> future : futures) {
            try {
                System.out.println(future.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Shutdown the executor service after some time to allow tasks to complete
        scheduledExecutorService.schedule(() -> {
            scheduledExecutorService.shutdown();
            System.out.println("ScheduledExecutorService shutdown");
        }, 60, TimeUnit.SECONDS);
    }
}