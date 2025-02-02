package ExecutorService.SingleThreadPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PolicyReminderService {
    public static void main(String[] args) {
        Customer[] customers = {
                new Customer("Alice", "alice@example.com"),
                new Customer("Bob", "bob@example.com"),
                new Customer("Charlie", "charlie@example.com"),
                new Customer("David", "david@example.com"),
                new Customer("Eve", "eve@example.com")
        };

        // Only single thread will execute all the tasks one by one
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        // the above line is the same as below
//        ExecutorService executorService = Executors.newFixedThreadPool(1);

        for (Customer customer : customers) {
            PolicyReminderTask task = new PolicyReminderTask(customer);
            executorService.submit(task);
        }

        executorService.shutdown();
    }
}
