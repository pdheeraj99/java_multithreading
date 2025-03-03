package Volatility;

/**
 * The whiteboard analogy helps visualize:

	Volatile = everyone can see the current value (glasses on)
	Atomic = only one person can change the value at a time (exclusive marker)
 * 
 * 
 * VolatileVsAtomicDemo.java
 * 
 * CONCEPT: Volatile vs Atomic Operations
 * -------------------------------------
 * 
 * VOLATILE:
 * - Ensures visibility of changes across threads (memory consistency)
 * - Does NOT make operations atomic
 * - Changes made by one thread are immediately visible to all threads
 * - Does not prevent race conditions for compound operations
 * 
 * ATOMICITY:
 * - Operations that complete in a single, uninterruptible step
 * - Cannot be interfered with by other threads
 * - Ensures that compound operations (like read-modify-write) are treated as one unit
 * - Provided by classes in java.util.concurrent.atomic or synchronized blocks
 * 
 * REAL-WORLD ANALOGY:
 * 
 * Imagine a whiteboard in an office (shared variable):
 * 
 * VOLATILE is like ensuring everyone has their glasses on to see the board:
 * - Everyone can SEE what's currently on the whiteboard
 * - But two people might try to update it at the same time
 * - Person A reads "5", adds 1, starts writing "6"
 * - Person B also reads "5", adds 1, also writes "6"
 * - Final result is 6, not 7 (lost update)
 * 
 * ATOMIC is like having a marker that only one person can hold at a time:
 * - Only the person with the marker can update the whiteboard
 * - Person A gets marker, reads "5", adds 1, writes "6", puts marker down
 * - Person B waits, then gets marker, reads "6", adds 1, writes "7"
 * - Final result is correctly 7
 */

 import java.util.concurrent.atomic.AtomicInteger;
 import java.util.concurrent.ExecutorService;
 import java.util.concurrent.Executors;
 import java.util.concurrent.TimeUnit;
 
 public class VolatileVsAtomicDemo {
	  
	  // PROBLEM SCENARIO: Each thread increments a counter 1000 times
	  
	  // Approach 1: Regular variable (has visibility AND atomicity problems)
	  private static int regularCounter = 0;
	  
	  // Approach 2: Volatile variable (fixes visibility but NOT atomicity)
	  private static volatile int volatileCounter = 0;
	  
	  // Approach 3: AtomicInteger (fixes both visibility AND atomicity)
	  private static AtomicInteger atomicCounter = new AtomicInteger(0);
	  
	  // Approach 4: Synchronized method (also fixes both issues)
	  private static int syncCounter = 0;
	  private static synchronized void incrementSyncCounter() {
			syncCounter++;
	  }
	  
	  public static void main(String[] args) throws InterruptedException {
			// Create a thread pool with 10 threads
			ExecutorService executor = Executors.newFixedThreadPool(10);
			
			// Each thread will increment each counter 1000 times
			for (int i = 0; i < 10; i++) {
				 executor.submit(() -> {
					  for (int j = 0; j < 1000; j++) {
							// Increment regular counter (problem: not atomic)
							regularCounter++;
							
							// Increment volatile counter (problem: ++ is not atomic)
							volatileCounter++;
							
							// Increment atomic counter (correct: atomic operation)
							atomicCounter.incrementAndGet();
							
							// Increment with synchronized method (correct: atomic)
							incrementSyncCounter();
					  }
				 });
			}
			
			// Shutdown the executor and wait for all tasks to complete
			executor.shutdown();
			executor.awaitTermination(10, TimeUnit.SECONDS);
			
			// Expected result: 10,000 for each counter (10 threads × 1000 increments)
			System.out.println("Regular counter:  " + regularCounter + 
									" (WRONG: race conditions & visibility issues)");
			
			System.out.println("Volatile counter: " + volatileCounter + 
									" (WRONG: solves visibility but still has race conditions)");
			
			System.out.println("Atomic counter:   " + atomicCounter.get() + 
									" (CORRECT: atomic operations prevent race conditions)");
			
			System.out.println("Sync counter:     " + syncCounter + 
									" (CORRECT: synchronized methods prevent race conditions)");
			
			// Explanation of the increment problem:
			explainIncrementProblem();
	  }
	  
	  /**
		* Explains why ++ operation is not atomic and can cause problems.
		*/
	  private static void explainIncrementProblem() {
			System.out.println("\nWHY DOES THIS HAPPEN? The ++ operation is not atomic!\n");
			System.out.println("What happens with volatile/regular variable:");
			System.out.println("1. Thread A reads value (e.g., 42)");
			System.out.println("2. Thread B also reads value (also 42)");
			System.out.println("3. Thread A adds 1, writes 43");
			System.out.println("4. Thread B adds 1 to its read value, also writes 43");
			System.out.println("5. Counter should be 44, but it's actually 43!\n");
			
			System.out.println("What happens with atomic/synchronized:");
			System.out.println("1. Thread A locks, reads 42, adds 1, writes 43, unlocks");
			System.out.println("2. Thread B waits, then locks, reads 43, adds 1, writes 44, unlocks");
			System.out.println("3. Counter is correctly 44");
	  }
 }
 
 /* DEEPER UNDERSTANDING:
 
	 1. The Increment Problem (x++) is a 3-step operation:
		 - READ the current value
		 - ADD 1 to it
		 - WRITE the new value
		 This creates a window where another thread can interfere.
	 
	 2. Volatile guarantees:
		 - Changes are visible immediately to other threads
		 - Prevents compiler optimizations that could reorder operations
		 - DOES NOT make compound operations atomic
	 
	 3. Atomic classes provide:
		 - Operations that cannot be interrupted
		 - Methods like incrementAndGet() that do entire read-modify-write as one unit
		 - Better performance than synchronized in many cases
	 
	 4. When to use volatile:
		 - For simple flags where you only write once (e.g., isRunning = false)
		 - When you only need visibility but not atomicity
		 - For reference variables that you want to be visible when changed
		 
	 5. When to use atomic:
		 - For counters that are updated by multiple threads
		 - When you need atomic read-modify-write operations
		 - For compare-and-set operations
		 
	 6. When to use synchronized:
		 - For more complex operations involving multiple variables
		 - When you need to ensure several operations happen together
		 - For general thread safety of methods
 */