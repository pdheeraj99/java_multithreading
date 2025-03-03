package ThreadLocal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.function.Supplier;

public class Complete_Concept_Example {

    // KEY POINT 1: Basic ThreadLocal - each thread gets its own instance
    // Use case: Thread-specific user IDs in web applications
    private static ThreadLocal<Integer> userIdThreadLocal = new ThreadLocal<>();

    // KEY POINT 2: ThreadLocal with initialValue() - provides default values
    // Use case: Thread-specific database connections
    private static ThreadLocal<String> connectionThreadLocal = ThreadLocal.withInitial(() -> {
        String connectionId = "CONN-" + System.nanoTime();
        System.out.println("Creating new connection: " + connectionId + " for thread: " + Thread.currentThread().getName());
        return connectionId;
    });

    // KEY POINT 3: ThreadLocal using lambda supplier - modern alternative for initialization
    // Use case: Expensive or complex initial objects like date formatters (not thread-safe)
    private static ThreadLocal<SimpleDateFormat> dateFormatterThreadLocal = 
        ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    // KEY POINT 4: InheritableThreadLocal - child threads inherit values from parent
    // Use case: Maintaining context like transaction IDs across child threads
    private static InheritableThreadLocal<String> transactionIdThreadLocal = 
        new InheritableThreadLocal<String>() {
            @Override
            protected String initialValue() {
                return "NO_TRANSACTION";
            }
            
            // Optional: Customize how values are inherited by child threads
            @Override
            protected String childValue(String parentValue) {
                return parentValue + "-child";
            }
        };

    public static void main(String[] args) throws InterruptedException {
        // KEY POINT 5: ThreadLocal is perfect for thread safety without synchronization
        System.out.println("=== ThreadLocal Master Example ===");
        
        // KEY POINT 6: Set value in main thread
        userIdThreadLocal.set(1000);
        transactionIdThreadLocal.set("MAIN-TRANSACTION");
        System.out.println("Main thread values set: userId=" + userIdThreadLocal.get() + 
                          ", transactionId=" + transactionIdThreadLocal.get());
        
        // Create multiple threads to demonstrate ThreadLocal isolation
        int threadCount = 3;
        CountDownLatch latch = new CountDownLatch(threadCount);
        
        for (int i = 1; i <= threadCount; i++) {
            final int threadId = i;
            // Create and start worker threads
            Thread workerThread = new Thread(() -> {
                try {
                    // KEY POINT 7: Each thread has its own independent copy of ThreadLocal values
                    // This shows thread isolation - core benefit of ThreadLocal
                    userIdThreadLocal.set(threadId);
                    
                    // KEY POINT 8: No need to initialize, withInitial() provides default value
                    String connection = connectionThreadLocal.get();
                    
                    // KEY POINT 9: Thread-safe usage of non-thread-safe objects (like SimpleDateFormat)
                    String timestamp = dateFormatterThreadLocal.get().format(new Date());
                    
                    System.out.println(String.format("[Thread-%d] timestamp: %s, userId: %d, connection: %s, " +
                                                    "inherited transactionId: %s",
                                                    threadId, timestamp, userIdThreadLocal.get(), 
                                                    connection, transactionIdThreadLocal.get()));
                    
                    // KEY POINT 10: Creating a child thread demonstrates InheritableThreadLocal
                    Thread childThread = new Thread(() -> {
                        // Child inherits transaction ID but has its own separate userId and connection
                        System.out.println(String.format("  [Child of Thread-%d] userId: %s, " + 
                                                       "connection: %s, inherited transactionId: %s",
                                                       threadId,
                                                       userIdThreadLocal.get(), // Will be null - not inherited
                                                       connectionThreadLocal.get(), // New connection created
                                                       transactionIdThreadLocal.get())); // Inherited and modified
                    });
                    childThread.start();
                    childThread.join();
                    
                    // Simulate thread work
                    Thread.sleep(1000);
                    
                    // KEY POINT 11: Modifying ThreadLocal doesn't affect other threads
                    userIdThreadLocal.set(threadId + 100);
                    System.out.println("[Thread-" + threadId + "] Modified userId: " + userIdThreadLocal.get());
                    
                    // KEY POINT 12: CRITICAL - Always clean up ThreadLocal to prevent memory leaks
                    // Each ThreadLocal value is stored in the Thread object itself, and won't be
                    // garbage collected until the thread dies if not removed manually
                    userIdThreadLocal.remove();
                    connectionThreadLocal.remove();
                    dateFormatterThreadLocal.remove();
                    transactionIdThreadLocal.remove();
                    
                    System.out.println("[Thread-" + threadId + "] Cleaned up ThreadLocal resources");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            }, "Worker-" + threadId);
            
            workerThread.start();
        }
        
        // Wait for all threads to complete
        latch.await();
        
        // KEY POINT 13: Main thread's values remain unchanged - demonstrating thread isolation
        System.out.println("Main thread values unchanged: userId=" + userIdThreadLocal.get() + 
                          ", transactionId=" + transactionIdThreadLocal.get());
        
        // KEY POINT 14: Memory leak demo with thread pools (commented out for safety)
        /*
        // DANGER: If using thread pools, failing to remove ThreadLocals causes memory leaks
        // because pooled threads live for the duration of the application
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 100; i++) {
            executorService.submit(() -> {
                userIdThreadLocal.set(999); // If not removed, this stays in thread memory
                // Missing userIdThreadLocal.remove(); - memory leak!
                return null;
            });
        }
        */
        
        // KEY POINT 15: Best practices summary - always clean up!
        System.out.println("\n=== ThreadLocal Best Practices ===");
        System.out.println("1. Always call remove() when done (especially in thread pools)");
        System.out.println("2. Use try-finally to ensure cleanup even with exceptions");
        System.out.println("3. Consider using withInitial() for default values");
        System.out.println("4. Use InheritableThreadLocal when parent-child sharing is needed");
        System.out.println("5. ThreadLocal doesn't replace thread synchronization for shared data");
        System.out.println("6. Use ThreadLocal for truly thread-specific data only");
    }
    
    // KEY POINT 16: Real-world use case utility method
    // This demonstrates a common pattern seen in production frameworks
    public static <T> T executeWithTransaction(Supplier<T> action, String transactionName) {
        // Set up transaction context
        String oldTransaction = transactionIdThreadLocal.get();
        transactionIdThreadLocal.set(transactionName);
        
        try {
            // Execute the provided action within this transaction context
            return action.get();
        } finally {
            // Restore previous transaction or clean up
            if (oldTransaction != null) {
                transactionIdThreadLocal.set(oldTransaction);
            } else {
                transactionIdThreadLocal.remove();
            }
        }
    }
}