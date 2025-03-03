package DaemonThreads;

public class BasicDaemonExample {
	public static void main(String[] args) {
		 // Create a daemon thread (like a background service)
		 Thread backgroundService = new Thread(() -> {
			  while (true) {
					try {
						 System.out.println("Daemon thread is running in background...");
						 Thread.sleep(1000);
					} catch (InterruptedException e) {
						 e.printStackTrace();
					}
			  }
		 });
		 
		 // Set as daemon before starting
		 // Similar to how background music in a game stops when you exit the game
		 backgroundService.setDaemon(true);
		 backgroundService.start();
		 
		 System.out.println("Main thread is working...");
		 try {
			  // Main thread works for 3 seconds
			  Thread.sleep(3000);
		 } catch (InterruptedException e) {
			  e.printStackTrace();
		 }
		 
		 // When main exits, the daemon thread will be terminated automatically
		 System.out.println("Main thread is exiting. Daemon will be terminated.");
		 // No need to explicitly stop the daemon thread
	}
}