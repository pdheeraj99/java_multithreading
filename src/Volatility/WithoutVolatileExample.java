package Volatility;

// Without Volatile:

// JVM might optimize and cache stopRequested in the worker thread
// Worker may never see that stopRequested changed and run forever
// The behavior is non-deterministic and depends on the JVM implementation
// Sometimes it might work by chance (JVM flushes caches) but it's not guaranteed

public class WithoutVolatileExample {
	// WITHOUT volatile - the worker may never see changes to this variable
	private boolean stopRequested = false;
	
	public void runExample() {
		 // Create a worker thread that checks the flag in a loop
		 Thread workerThread = new Thread(() -> {
			  long count = 0;
			  System.out.println("Worker started, but might run forever without volatile!");
			  
			  // This loop might NEVER exit because the worker thread may 
			  // cache the value of stopRequested and never see the update
			  while (!stopRequested) {
					count++; // Just counting to simulate work
			  }
			  
			  // This line might never be reached without volatile!
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
		 
		 // Signal the worker to stop - BUT worker might never see this change!
		 System.out.println("Main: Requesting worker to stop (but worker might not see this!)");
		 stopRequested = true;
		 
		 // Wait for worker to complete - might hang forever!
		 System.out.println("Main: Waiting for worker to terminate (might wait forever!)");
		 try {
			  // Adding a timeout so the program doesn't hang forever
			  workerThread.join(5000); 
			  
			  if (workerThread.isAlive()) {
					System.out.println("Main: Worker is STILL RUNNING after 5 seconds!");
					System.out.println("Main: This demonstrates the problem without volatile!");
					// Force exit the program since it might run forever
					System.out.println("Main: Forcing program exit");
					System.exit(0);
			  } else {
					System.out.println("Main: Worker has terminated normally");
					System.out.println("Main: We got lucky - the JVM happened to flush the value!");
			  }
		 } catch (InterruptedException e) {
			  e.printStackTrace();
		 }
	}
	
	public static void main(String[] args) {
		 new WithoutVolatileExample().runExample();
	}
}