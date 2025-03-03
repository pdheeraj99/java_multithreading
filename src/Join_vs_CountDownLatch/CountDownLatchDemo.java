package Join_vs_CountDownLatch;

/**
 * CountDownLatchDemo.java - Understanding CountDownLatch
 * 
 * CONCEPT: CountDownLatch
 * -----------------------
 * A synchronization aid that allows one or more threads to wait until a set of operations 
 * being performed in other threads completes. It's initialized with a count and blocks 
 * until the count reaches zero.
 * 
 * REAL-WORLD ANALOGY: 
 * Imagine a teacher (main thread) with 3 students (worker threads) taking a test:
 * - The teacher announces: "Nobody leaves until all 3 tests are submitted"
 * - The teacher creates a "counter" (CountDownLatch) set to 3
 * - Each student works independently at their own pace
 * - When a student finishes, they submit their test (countdown)
 * - The teacher cannot dismiss class (waits at await) until all 3 tests are submitted
 * - Once count reaches zero, the teacher can proceed with the next activity
 * 
 * Another analogy is a team leader waiting for all team members to arrive before starting a meeting:
 * - Team leader sets up a CountDownLatch with count = team size
 * - Each team member arrives and checks in (countdown)
 * - Meeting (main thread) waits at await() until everyone has arrived
 * - When the last person checks in, the latch opens and meeting starts
 */
import java.util.concurrent.CountDownLatch;

public class CountDownLatchDemo {
    public static void main(String[] args) {
        // Number of tasks we want to wait for
        int taskCount = 3;
        
        // Create a CountDownLatch initialized with our count
        // Think of this as "3 tasks must complete before we can proceed"
        CountDownLatch latch = new CountDownLatch(taskCount);
        
        System.out.println("Main: Starting tasks and waiting for all to complete");
        
        // Create and start multiple worker threads
        for (int i = 1; i <= taskCount; i++) {
            // Each worker gets a copy of the same latch object
            final int taskId = i;
            
            // Create and start a worker thread
            new Thread(() -> {
                try {
                    System.out.println("Task " + taskId + ": Starting work");
                    
                    // Simulate work taking different amounts of time
                    // This is important: workers finish at DIFFERENT times
                    Thread.sleep(taskId * 1000); 
                    
                    System.out.println("Task " + taskId + ": Work complete, counting down latch");
                    
                    // KEY CONCEPT: countdown() decrements the latch counter by 1
                    // This is like a worker saying "I'm done with my part"
                    latch.countDown();
                    
                    // IMPORTANT: The thread continues executing after countdown()
                    // countdown() doesn't block the calling thread
                    System.out.println("Task " + taskId + ": Continued working after countdown");
                    
                } catch (InterruptedException e) {
                    System.out.println("Task " + taskId + ": Interrupted");
                }
            }).start();
        }
        
        try {
            // KEY CONCEPT: await() blocks the current thread until count reaches zero
            // This is the main thread saying "I'll wait here until all tasks signal completion"
            // Think of this as the teacher waiting for all tests to be submitted
            System.out.println("Main: Waiting for all " + taskCount + " tasks to finish");
            latch.await();
            
            // This code only executes after ALL tasks have called countDown()
            // Once the latch count reaches zero, await() returns
            System.out.println("Main: All tasks have completed, continuing execution");
            
        } catch (InterruptedException e) {
            System.out.println("Main: Interrupted while waiting");
        }
        
        // This statement only executes after all tasks have counted down
        System.out.println("Main: Program complete");
    }
}

/* DEEPER UNDERSTANDING OF CountDownLatch:
   
   1. One-Time Use:
      - CountDownLatch cannot be reset once count reaches zero
      - For repeatable scenarios, use CyclicBarrier instead
   
   2. Key Methods:
      - countDown(): Decrements count by 1 (non-blocking)
      - await(): Blocks until count reaches zero
      - await(timeout, unit): Blocks with timeout
      - getCount(): Returns current count
   
   3. Differences from join():
      - join() waits for ONE specific thread to complete
      - CountDownLatch waits for ANY N events to occur
      - Events could be from threads, external signals, etc.
   
   4. Key Characteristics:
      - You cannot increase the count once created
      - await() can be called by multiple threads (all will be released when count=0)
      - countDown() can be called by any number of threads
      
   5. Common Use Cases:
      - Starting a service only after all required services are up
      - Processing data only after all data chunks are loaded
      - Implementing a simple thread barrier (all threads reach point X before proceeding)
      - Testing performance with multiple threads starting at the same time
   
   6. Limitations:
      - Once released, cannot be reused
      - No built-in way to reset the counter
      - No way to know which thread called countDown()
*/