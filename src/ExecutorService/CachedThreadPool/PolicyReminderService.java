package ExecutorService.CachedThreadPool;

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

        ExecutorService executorService = Executors.newCachedThreadPool();

        for (Customer customer : customers) {
            PolicyReminderTask task = new PolicyReminderTask(customer);
            executorService.submit(task);
        }

        executorService.shutdown();
    }
}
