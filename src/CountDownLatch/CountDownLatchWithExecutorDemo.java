package CountDownLatch;

/**
 * CountDownLatchWithExecutorDemo.java - CountDownLatch with ExecutorService
 * 
 * CONCEPT: CountDownLatch with Executor Framework
 * ----------------------------------------------
 * This demonstrates how to use CountDownLatch with the Executor framework for 
 * better thread management. The ExecutorService manages a pool of worker threads, 
 * while the CountDownLatch coordinates when the tasks are complete.
 * 
 * REAL-WORLD ANALOGY: 
 * Think of a restaurant kitchen during dinner service:
 * - Head chef (main thread) assigns multiple dishes to cook (tasks)
 * - Line cooks (thread pool) work on different dishes simultaneously
 * - Expediter (CountDownLatch) tracks when all dishes for a table are ready
 * - Server (awaiting thread) waits until expediter confirms all dishes are done
 * - Once all dishes are ready (count = 0), the server delivers the complete order
 */
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CountDownLatchWithExecutorDemo {
    public static void main(String[] args) {
        // Number of tasks to be executed
        int taskCount = 4;
        
        // Create a CountDownLatch initialized with our task count
        CountDownLatch latch = new CountDownLatch(taskCount);
        
        // Create a fixed thread pool with 2 threads
        // Note: We're using fewer threads than tasks to demonstrate
        // how the executor reuses threads for multiple tasks
        ExecutorService executor = Executors.newFixedThreadPool(2);
        
        System.out.println("Main: Submitting " + taskCount + 
                           " tasks to be executed by " + 2 + " threads");
        
        // Submit tasks to the executor
        for (int i = 1; i <= taskCount; i++) {
            final int taskId = i;
            
            // Submit a Runnable task to the executor
            // The executor will assign this task to an available thread from the pool
            executor.submit(() -> {
                try {
                    System.out.println("Task " + taskId + 
                                      ": Started by thread " + 
                                      Thread.currentThread().getName());
                    
                    // Simulate work with different durations
                    long workTime = 1000 + (taskId * 500);
                    Thread.sleep(workTime);
                    
                    System.out.println("Task " + taskId + 
                                      ": Completed after " + workTime + "ms" +
                                      " by thread " + Thread.currentThread().getName());
                    
                    // Task is complete, count down the latch
                    latch.countDown();
                    
                    // Current count after this task completes
                    System.out.println("Remaining tasks: " + latch.getCount());
                    
                } catch (InterruptedException e) {
                    System.out.println("Task " + taskId + ": Interrupted");
                    // Even if a task fails, we need to count down
                    latch.countDown();
                }
            });
        }
        
        try {
            // Block the main thread until all tasks are done
            System.out.println("Main: Waiting for all tasks to complete");
            
            // This is where the main thread waits
            latch.await();
            
            System.out.println("Main: All tasks have completed!");
            
        } catch (InterruptedException e) {
            System.out.println("Main: Interrupted while waiting");
        } finally {
            // Proper shutdown of the executor service
            // This is very important - otherwise the program won't exit!
            System.out.println("Main: Shutting down the executor");
            
            // Prevent new tasks from being submitted
            executor.shutdown();
            
            try {
                // Wait a while for existing tasks to terminate
                if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                    // Cancel currently executing tasks forcefully
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                // If current thread also interrupted, cancel tasks
                executor.shutdownNow();
                // Preserve interrupt status
                Thread.currentThread().interrupt();
            }
            
            System.out.println("Main: Executor has been shut down");
        }
        
        System.out.println("Main: Program complete");
    }
}

/* DEEPER UNDERSTANDING OF CountDownLatch WITH EXECUTOR:
   
   1. Thread Pool vs. CountDownLatch:
      - Thread pool (ExecutorService) manages the EXECUTION of tasks
      - CountDownLatch manages COORDINATION between tasks
      - They solve different problems but work well together
   
   2. Benefits of using ExecutorService with CountDownLatch:
      - Better resource management (reuse threads instead of creating new ones)
      - Separates task execution from task coordination
      - More efficient for large numbers of tasks
      - Built-in task queuing when all threads are busy
   
   3. Important Patterns:
      - Always shutdown the executor when done (executor.shutdown())
      - Remember that countDown() must be called regardless of task success or failure
      - Sometimes you need to count down in a finally block
   
   4. Advanced Considerations:
      - If tasks throw exceptions, they could prevent countdown
      - Consider adding try/catch/finally to ensure latch counts down
      - For complex scenarios, use CompletableFuture instead
      
   5. Thread Pool Types for Different Needs:
      - Fixed Thread Pool (as shown): Stable, predetermined number of threads
      - Cached Thread Pool: Flexible, creates new threads as needed
      - Scheduled Thread Pool: For tasks that need to run at specific times
      - Single Thread Executor: Guarantees sequential execution
*/