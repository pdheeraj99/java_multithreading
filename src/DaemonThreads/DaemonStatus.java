package DaemonThreads;

public class DaemonStatus {
	public static void main(String[] args) {
		 // Creating a thread - like setting up a task
		 Thread thread = new Thread(() -> {
			  System.out.println("Inside thread, daemon status: " + Thread.currentThread().isDaemon());
			  // You cannot change daemon status after the thread has started
			  try {
					Thread.sleep(2000);
			  } catch (InterruptedException e) {
					e.printStackTrace();
			  }
		 });
		 
		 // Check default daemon status (inherits from parent thread)
		 System.out.println("Before setting: Is daemon? " + thread.isDaemon());
		 
		 // Set as daemon - like making it a background process
		 thread.setDaemon(true);
		 System.out.println("After setting: Is daemon? " + thread.isDaemon());
		 
		 thread.start();
		 
		 // Will throw IllegalThreadStateException if uncommented
		 // thread.setDaemon(false); // Cannot change daemon status after starting
		 
		 try {
			  // Give thread time to print its message
			  Thread.sleep(500);
		 } catch (InterruptedException e) {
			  e.printStackTrace();
		 }
	}
}