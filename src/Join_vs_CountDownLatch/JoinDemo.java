package Join_vs_CountDownLatch;

/**
 * JoinDemo.java - Understanding Thread.join()
 * 
 * CONCEPT: Thread.join()
 * -------------------
 * The join() method forces the current thread to wait until the thread it's called on completes.
 * 
 * REAL-WORLD ANALOGY: 
 * Think of two friends (threads) planning to meet at a restaurant:
 * - Friend A (worker thread) goes inside to get a table
 * - Friend B (main thread) waits outside until Friend A texts "I got a table"
 * - Friend B can't go in and sit until Friend A completes the task of getting a table
 * - The "join()" is like Friend B saying "I'll wait right here until you text me"
 * 
 * Another analogy is a parent waiting for their child to finish homework:
 * - Parent (main thread) tells child (worker thread) to do homework
 * - Parent uses join() to say "I won't start our movie night until your homework is done"
 * - Parent thread blocks/waits at join() until child thread completes its task
 */
public class JoinDemo {
	public static void main(String[] args) {
		 // Create a simple worker thread
		 Thread workerThread = new Thread(() -> {
			  // This code runs in a separate thread (like a different person doing a separate task)
			  System.out.println("Worker: I'm starting my task");
			  
			  // Simulate some work with sleep
			  try {
					Thread.sleep(2000); // Worker does 2 seconds of work
					// During this sleep, the worker thread is active but busy
					// Meanwhile, the main thread will be waiting at join() if we've reached that point
			  } catch (InterruptedException e) {
					System.out.println("Worker: I was interrupted");
			  }
			  
			  System.out.println("Worker: I've finished my task");
			  // At this point, any thread waiting on this worker's join() will be allowed to continue
		 });
		 
		 // Start the worker thread
		 System.out.println("Main: Starting the worker");
		 workerThread.start(); // Worker thread begins its separate execution path
		 
		 System.out.println("Main: Now I'll wait for worker to finish");
		 
		 try {
			  // This is where the join() magic happens:
			  // 1. The main thread calls join() on workerThread
			  // 2. Main thread STOPS here and enters a waiting state
			  // 3. Main thread will not execute any more code past this point
			  //    until workerThread completely finishes its run() method
			  workerThread.join();
			  
			  // When execution reaches this point, we have a 100% guarantee
			  // that workerThread has completed its entire run() method.
			  // It's like waiting at the finish line - you only proceed when
			  // the runner completes the race.
		 } catch (InterruptedException e) {
			  // This happens if something interrupts the main thread while it's waiting
			  System.out.println("Main: I was interrupted while waiting");
		 }
		 
		 // This code will only run AFTER workerThread is done
		 // Without join(), this might run before worker finishes
		 System.out.println("Main: Worker has finished, now I can continue");
	}
}

/* DEEPER UNDERSTANDING OF join():
  
  1. Thread States During join():
	  - Calling thread (main): Enters WAITING state
	  - Target thread (worker): Unaffected, continues execution
  
  2. What join() Guarantees:
	  - All code in the target thread will complete before the calling thread continues
	  - Any changes made by the target thread will be visible to the calling thread
  
  3. Join with Timeout - join(1000):
	  - Will only wait UP TO 1000ms
	  - If thread finishes sooner, join() returns immediately
	  - If thread takes longer, join() returns anyway after timeout
  
  4. Use Cases:
	  - When a thread depends on results calculated by another thread
	  - When threads need to complete in a specific order
	  - When you need to ensure all worker threads finish before proceeding
	  
  5. Without join():
	  - Main thread would continue executing without waiting
	  - Could lead to race conditions if main thread needs worker's results
	  - Program might end before worker thread completes its task
*/