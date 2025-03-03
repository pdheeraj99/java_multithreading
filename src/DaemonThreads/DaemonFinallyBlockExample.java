package DaemonThreads;

public class DaemonFinallyBlockExample {
	public static void main(String[] args) {
		 // Demonstrate that daemon threads don't execute finally blocks when JVM exits
		 
		 Thread daemonWithFinally = new Thread(() -> {
			  try {
					System.out.println("Daemon thread started");
					// Simulate long-running process
					Thread.sleep(10000); // 10 seconds
					System.out.println("This won't print");
			  } catch (InterruptedException e) {
					System.out.println("Daemon thread interrupted");
			  } finally {
					// Critical cleanup code that may not execute if JVM exits
					System.out.println("Daemon thread finally block - THIS MAY NOT EXECUTE!");
					// Resource cleaning operations here would be risky
			  }
		 });
		 daemonWithFinally.setDaemon(true);
		 
		 Thread nonDaemonWithFinally = new Thread(() -> {
			  try {
					System.out.println("Non-daemon thread started");
					// Shorter process
					Thread.sleep(5000); // 5 seconds
					System.out.println("Non-daemon thread completed its work");
			  } catch (InterruptedException e) {
					System.out.println("Non-daemon thread interrupted");
			  } finally {
					// This will always execute
					System.out.println("Non-daemon thread finally block - THIS WILL ALWAYS EXECUTE");
			  }
		 });
		 
		 daemonWithFinally.start();
		 nonDaemonWithFinally.start();
		 
		 try {
			  // Wait for non-daemon to finish (but not waiting for daemon)
			  nonDaemonWithFinally.join();
			  
			  System.out.println("Main thread exiting. Daemon thread will be terminated!");
			  // JVM will exit here, potentially not executing the finally block in daemon
		 } catch (InterruptedException e) {
			  e.printStackTrace();
		 }
	}
}