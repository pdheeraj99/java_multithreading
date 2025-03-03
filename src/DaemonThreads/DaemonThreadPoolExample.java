package DaemonThreads;

import java.util.concurrent.*;

public class DaemonThreadPoolExample {
    public static void main(String[] args) {
        // Create a thread factory that produces daemon threads
        ThreadFactory daemonFactory = r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);  // Make all threads from this factory daemons
            return t;
        };
        
        // Create a thread pool with daemon threads - like a team of background helpers
        ExecutorService executorService = Executors.newFixedThreadPool(3, daemonFactory);
        
        // Submit multiple monitoring tasks to the daemon pool
        for (int i = 0; i < 5; i++) {
            final int taskId = i;
            executorService.submit(() -> {
                try {
                    // Simulate some background monitoring activity
                    for (int j = 0; j < 10; j++) {
                        System.out.println("Background task #" + taskId + 
                                          " - monitoring iteration " + j);
                        Thread.sleep(700);
                    }
                } catch (InterruptedException e) {
                    System.out.println("Task #" + taskId + " was interrupted");
                }
                return "Task " + taskId + " completed";  // This may never be returned
            });
        }
        
        // Don't call shutdown() for the executor, let JVM terminate it
        System.out.println("Main application logic running...");
        
        try {
            // Main application runs for 5 seconds
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("Main application exiting. All daemon threads will terminate.");
        // No need to wait for tasks to complete as they're daemon threads
    }
}