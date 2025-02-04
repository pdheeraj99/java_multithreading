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

//        scheduleAtFixedRate: Executes every 10 seconds, regardless of how long the task takes to complete.
//        scheduleWithFixedDelay: Executes 15 seconds after the previous execution finishes.

        // Schedule Runnable tasks at a fixed rate
        // scheduleAtFixedRate: Schedules a task to run at a fixed rate. The task is executed at regular intervals, starting after the initial delay. The interval is measured from the start of one execution to the start of the next.
        for (Customer customer : customers) {
            PolicyReminderRunnableTask runnableTask = new PolicyReminderRunnableTask(customer);
            // Task runs every 10 seconds, starting immediately
            scheduledExecutorService.scheduleAtFixedRate(runnableTask, 0, 10, TimeUnit.SECONDS);
        }

        // scheduleWithFixedDelay: Schedules a task to run with a fixed delay between the end of one execution and the start of the next. The task is executed after the initial delay, and subsequent executions are delayed by the specified period after the previous execution completes.
        // Schedule a periodic Runnable task with a fixed delay
        scheduledExecutorService.scheduleWithFixedDelay(() -> {
            System.out.println("Running periodic task with fixed delay");
        }, 0, 15, TimeUnit.SECONDS);
// Task runs 15 seconds after the previous execution completes, starting immediately

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