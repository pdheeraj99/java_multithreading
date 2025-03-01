package Volatility;

// With Volatile:
// Clear Explanation At the END of file ( there is only one thread u created right in the example then how come other thread ? )
// Change to stopRequested is guaranteed to be visible to the worker thread
// Worker will exit its loop shortly after the main thread sets the flag

public class WithVolatileExample {
	// Using volatile ensures visibility across threads
	private volatile boolean stopRequested = false;
	
	public void runExample() {
		 // Create a worker thread that checks the flag in a loop
		 Thread workerThread = new Thread(() -> {
			  long count = 0;
			  System.out.println("Worker started, will run until stop is requested");
			  
			  // This loop will exit when the main thread sets stopRequested = true
			  while (!stopRequested) {
					count++; // Just counting to simulate work
			  }
			  
			  System.out.println("Worker detected stop signal and is exiting. Counted to: " + count);
		 });
		 
		 // Start the worker thread
		 workerThread.start();
		 System.out.println("Main: Worker thread started");
		 
		 // Sleep for a second to let the worker run
		 try {
			  Thread.sleep(1000);
		 } catch (InterruptedException e) {
			  e.printStackTrace();
		 }
		 
		 // Signal the worker to stop
		 System.out.println("Main: Requesting worker to stop");
		 stopRequested = true;
		 
		 // Wait for worker to complete
		 try {
			  workerThread.join();
			  System.out.println("Main: Worker has terminated");
		 } catch (InterruptedException e) {
			  e.printStackTrace();
		 }
	}
	
	public static void main(String[] args) {
		 new WithVolatileExample().runExample();
	}
}

// Explanation

// The Thread Situation in the Examples
// In the WorkerThread example, there are actually two threads involved, even though only one is explicitly created:

// The Main Thread: This is created automatically when your Java program starts
// The Worker Thread: The thread that extends Thread class (the one we explicitly create)
// How It Works:
// When you have code like this:

// public class Main {
// 	public static void main(String[] args) {
// 		 // This runs in the main thread
// 		 WorkerThread worker = new WorkerThread();
// 		 worker.start(); // This creates and starts a new thread
		 
// 		 // Later, still in the main thread
// 		 worker.stopWorker(); // The main thread sets terminated = true
// 	}
// }

// Here's what happens:

// The main thread creates a WorkerThread object
// When worker.start() is called, a new thread begins executing the run() method
// Later, the main thread calls worker.stopWorker(), setting terminated = true
// The worker thread sees this change (because it's volatile) and exits its loop
// Without volatile, the worker thread might keep running forever, never seeing that the main thread changed terminated.

// So there are two threads:
// One thread is running the run() method in a loop
// Another thread (typically the main thread) calls stopWorker() to signal termination
// This is why volatile is necessary - it ensures communication between these two separate threads.