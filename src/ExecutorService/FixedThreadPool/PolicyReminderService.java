package ExecutorService.FixedThreadPool;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class PolicyReminderService {
    public static void main(String[] args) {
        Customer[] customers = {
                new Customer("Alice", "alice@example.com"),
                new Customer("Bob", "bob@example.com"),
                new Customer("Charlie", "charlie@example.com"),
                new Customer("David", "david@example.com"),
                new Customer("Eve", "eve@example.com")
        };

        ExecutorService executorService = Executors.newFixedThreadPool(3);

        for (Customer customer : customers) {
            PolicyReminderTask task = new PolicyReminderTask(customer);
            executorService.submit(task);
        }

        executorService.shutdown();
    }
}
