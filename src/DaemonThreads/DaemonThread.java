package DaemonThreads;

/**
 * DaemonThreadDemo - Complete guide to daemon threads
 * 
 * DEFINITION:
 * - Daemon threads are background threads with low priority
 * - JVM exits when all user threads finish, even if daemon threads are still running
 * - Used for background tasks that don't need to complete
 */
public class DaemonThread {
    
	public static void main(String[] args) throws InterruptedException {
		 System.out.println("=== DAEMON THREAD DEMONSTRATION ===");
		 
		 // Create a daemon thread that will run forever
		 Thread daemonThread = new Thread(() -> {
			  int count = 0;
			  
			  // This is an infinite loop - in a normal thread, this would prevent
			  // the JVM from ever exiting
			  while (true) {
					try {
						 Thread.sleep(1000);
						 count++;
						 System.out.println("Daemon thread is working... count: " + count);
						 
						 // This demonstrates that daemon threads run normally,
						 // they just don't prevent JVM exit
					} catch (InterruptedException e) {
						 System.out.println("Daemon thread interrupted");
						 break;
					}
			  }
		 });
		 
		 // KEY POINT #1: Must set daemon status BEFORE starting the thread
		 // Attempting to set daemon status after starting will throw IllegalThreadStateException
		 daemonThread.setDaemon(true);
		 
		 // KEY POINT #2: We can check if a thread is a daemon
		 System.out.println("Is our thread a daemon? " + daemonThread.isDaemon());
		 
		 // Start the daemon thread
		 daemonThread.start();
		 
		 // Create a regular user thread for comparison
		 Thread userThread = new Thread(() -> {
			  try {
					System.out.println("User thread started - will run for 5 seconds");
					Thread.sleep(5000);
					System.out.println("User thread finished");
			  } catch (InterruptedException e) {
					System.out.println("User thread interrupted");
			  }
		 });
		 
		 // KEY POINT #3: By default, new threads inherit daemon status from their creator
		 // This thread will be a user thread because main is a user thread
		 System.out.println("Is user thread a daemon by default? " + userThread.isDaemon());
		 
		 // Start the user thread
		 userThread.start();
		 
		 // KEY POINT #4: Let's create another daemon thread using a different way
		 Thread anotherDaemon = new Thread(() -> {
			  try {
					System.out.println("Another daemon thread started");
					// This logic would not complete if JVM exits early
					for (int i = 0; i < 10; i++) {
						 Thread.sleep(500);
						 System.out.println("Another daemon count: " + i);
					}
					System.out.println("Another daemon finished");
			  } catch (InterruptedException e) {
					System.out.println("Another daemon interrupted");
			  }
		 });
		 anotherDaemon.setDaemon(true);
		 anotherDaemon.start();
		 
		 // Wait for the user thread to finish
		 System.out.println("Main thread waiting for user thread to finish");
		 userThread.join();
		 
		 // KEY POINT #5: Daemon threads are automatically terminated when JVM exits
		 System.out.println("\nUser thread has finished. Main thread will now exit.");
		 System.out.println("Since there are no more user threads, JVM will exit");
		 System.out.println("and terminate all daemon threads, even though they're still running.");
		 
		 // Sleep briefly just to let the message print
		 Thread.sleep(100);
		 
		 // When main exits here, all daemon threads will be terminated
		 // No need to explicitly stop them
	}
	
	/**
	 * COMMON USE CASES FOR DAEMON THREADS:
	 * 
	 * 1. Garbage collection (JVM's GC runs as daemon thread)
	 * 2. Background cleanup operations that can terminate anytime
	 * 3. Service monitoring that runs for the application lifetime
	 * 4. Heartbeat signals for distributed systems
	 * 5. Auto-save functionality in applications
	 * 
	 * IMPORTANT NOTES:
	 * 
	 * 1. Never use daemon threads for critical operations that MUST complete
	 * 2. Daemon threads are terminated without running finally blocks
	 * 3. Avoid file/database operations in daemon threads that need to complete
	 * 4. Daemon threads are ideal for "nice to have" background services
	 * 5. If a thread creates another thread, the new thread inherits daemon status
	 */
	
	// Example of a practical daemon thread: Simple file cleanup service
	public static void startFileCleanupService() {
		 Thread cleanupThread = new Thread(() -> {
			  while (true) {
					try {
						 // Check for temporary files every hour
						 Thread.sleep(3600000);
						 
						 // In a real implementation, this would:
						 // 1. Scan temp directories
						 // 2. Delete files older than X days
						 System.out.println("Performing file cleanup");
						 
					} catch (InterruptedException e) {
						 break;
					}
			  }
		 });
		 
		 // Make it a daemon so it doesn't prevent application exit
		 cleanupThread.setDaemon(true);
		 cleanupThread.start();
		 
		 System.out.println("File cleanup service started as daemon thread");
	}
}