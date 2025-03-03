package DaemonThreads;
import java.util.Timer;
import java.util.TimerTask;

public class DaemonTimerExample {
    public static void main(String[] args) {
        System.out.println("Starting application...");
        
        // Create a timer with daemon thread - like an automatic watering system in a garden
        // that stops when you're away on vacation
        Timer timer = new Timer("ScheduledTaskTimer", true); // true makes it a daemon timer
        
        // Schedule a periodic task
        timer.schedule(new TimerTask() {
            private int count = 0;
            
            @Override
            public void run() {
                count++;
                System.out.println("[Timer] Executing scheduled task #" + count);
                
                // Simulate some periodic work
                if (count == 10) {
                    System.out.println("[Timer] Cancelling future tasks");
                    this.cancel(); // Stop this specific task after 10 executions
                }
            }
        }, 0, 1000); // Start immediately, repeat every second
        
        System.out.println("Timer scheduled. Main thread continuing...");
        
        try {
            // Main application runs for 5 seconds
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Uncomment to explicitly cancel the timer (not necessary with daemon timers)
        // timer.cancel();
        
        System.out.println("Main application exiting. Timer daemon will be terminated.");
    }
}