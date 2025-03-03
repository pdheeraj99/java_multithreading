package DaemonThreads;

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class DaemonThreadBestPractices {
    private static final AtomicBoolean shutdown = new AtomicBoolean(false);
    
    public static void main(String[] args) {
        System.out.println("Daemon Thread Best Practices Example");
        
        // GOOD PRACTICE: Provide a shutdown mechanism for daemon threads
        Thread goodDaemonThread = new Thread(() -> {
            while (!shutdown.get()) {
                try {
                    System.out.println("[Good Daemon] Performing background task...");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println("[Good Daemon] Interrupted, cleaning up...");
                    break;
                }
            }
            System.out.println("[Good Daemon] Clean shutdown complete");
        });
        goodDaemonThread.setDaemon(true);
        
        // BAD PRACTICE: Using daemon thread for critical file operations
        Thread badDaemonThread = new Thread(() -> {
            try {
                System.out.println("[Bad Daemon] Starting file operation...");
                // Simulate slow file operation
                Thread.sleep(5000);
                
                // This might never execute if the JVM exits
                try (FileWriter writer = new FileWriter("important_data.txt")) {
                    writer.write("Critical data that might be lost!");
                    System.out.println("[Bad Daemon] File written successfully");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (InterruptedException e) {
                System.out.println("[Bad Daemon] Interrupted during operation");
            }
        });
        badDaemonThread.setDaemon(true);
        
        // GOOD PRACTICE: Use non-daemon thread for critical operations
        Thread criticalThread = new Thread(() -> {
            try {
                System.out.println("[Critical Thread] Starting important work...");
                Thread.sleep(2000);
                System.out.println("[Critical Thread] Important work completed successfully");
            } catch (InterruptedException e) {
                System.out.println("[Critical Thread] Interrupted, but still completing critical tasks");
            }
            // This will execute even if main thread completes
            System.out.println("[Critical Thread] Cleanup complete");
        });
        // Not a daemon - will prevent JVM from exiting until it completes
        
        // Start all threads
        goodDaemonThread.start();
        badDaemonThread.start();
        criticalThread.start();
        
        try {
            System.out.println("Main thread working...");
            Thread.sleep(3000);
            System.out.println("Main thread signaling shutdown...");
            
            // Signal graceful shutdown to the good daemon thread
            shutdown.set(true);
            
            // Wait for critical thread to complete
            criticalThread.join();
            
            System.out.println("Main thread exiting. Bad daemon thread may not complete its file operation!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}