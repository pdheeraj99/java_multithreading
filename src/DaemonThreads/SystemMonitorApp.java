package DaemonThreads;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.*;

public class SystemMonitorApp {
    private static boolean applicationRunning = true;
    
    public static void main(String[] args) {
        System.out.println("Starting System Monitor Application...");
        
        // 1. CPU usage monitor (daemon) - like a car's temperature gauge
        Thread cpuMonitor = new Thread(() -> {
            while (true) {
                monitorCPU();
                try {
                    Thread.sleep(2000); 
                } catch (InterruptedException e) {
                    break;
                }
            }
        }, "CPU-Monitor");
        cpuMonitor.setDaemon(true);
        
        // 2. Memory usage monitor (daemon) - like fuel gauge in a car
        Thread memoryMonitor = new Thread(() -> {
            while (true) {
                monitorMemory();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }, "Memory-Monitor");
        memoryMonitor.setDaemon(true);
        
        // 3. Logger thread (daemon) - like a flight recorder in an aircraft
        Thread logger = new Thread(() -> {
            while (true) {
                logSystemStatus();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }, "System-Logger");
        logger.setDaemon(true);
        
        // 4. User interaction thread (non-daemon) - like a pilot who must land the plane
        Thread userInterface = new Thread(() -> {
            try {
                System.out.println("System monitoring active. Press Ctrl+C to exit.");
                // Simulate user interaction for 20 seconds
                for (int i = 0; i < 4; i++) {
                    Thread.sleep(5000);
                    System.out.println("\nUSER: Requesting system snapshot...");
                    printSystemSnapshot();
                }
                
                System.out.println("\nUSER: Shutting down monitoring application...");
                applicationRunning = false;
            } catch (InterruptedException e) {
                System.out.println("User interface interrupted.");
            }
        }, "User-Interface");
        // This is a non-daemon thread so the application waits for it
        
        // Start all threads
        cpuMonitor.start();
        memoryMonitor.start();
        logger.start();
        userInterface.start();
        
        // Main thread waits for user interface to complete
        try {
            userInterface.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("Application shutdown complete.");
        // All daemon threads will be terminated automatically
    }
    
    private static void monitorCPU() {
        if (!applicationRunning) return;
        double cpuUsage = Math.random() * 100; // Simulate CPU usage
        System.out.println("[CPU] Current usage: " + String.format("%.2f", cpuUsage) + "%");
    }
    
    private static void monitorMemory() {
        if (!applicationRunning) return;
        double memUsage = Math.random() * 16; // Simulate memory usage (GB)
        System.out.println("[MEMORY] Current usage: " + String.format("%.2f", memUsage) + " GB");
    }
    
    private static void logSystemStatus() {
        if (!applicationRunning) return;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("[LOG] System check at " + formatter.format(new Date()) + " - Status: NORMAL");
    }
    
    private static void printSystemSnapshot() {
        System.out.println("=============== SYSTEM SNAPSHOT ===============");
        System.out.println("CPU Usage: " + String.format("%.2f", Math.random() * 100) + "%");
        System.out.println("Memory Available: " + String.format("%.2f", Math.random() * 32) + " GB");
        System.out.println("Disk Space: " + String.format("%.2f", Math.random() * 900 + 100) + " GB");
        System.out.println("Network Status: " + (Math.random() > 0.1 ? "CONNECTED" : "DISCONNECTED"));
        System.out.println("=============================================");
    }
}