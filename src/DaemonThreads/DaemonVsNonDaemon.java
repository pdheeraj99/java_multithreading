package DaemonThreads;

public class DaemonVsNonDaemon {
	public static void main(String[] args) {
		 // Daemon thread - like a janitor who leaves when everyone else leaves
		 Thread daemonThread = new Thread(() -> {
			  for (int i = 0; i < 100; i++) {
					System.out.println("Daemon thread count: " + i);
					try {
						 Thread.sleep(500);
					} catch (InterruptedException e) {
						 e.printStackTrace();
					}
			  }
			  System.out.println("This message will never be printed from daemon thread");
		 });
		 daemonThread.setDaemon(true);

		 // Non-daemon thread - like a security guard who must complete their shift
		 Thread nonDaemonThread = new Thread(() -> {
			  for (int i = 0; i < 5; i++) {
					System.out.println("Non-daemon thread count: " + i);
					try {
						 Thread.sleep(500);
					} catch (InterruptedException e) {
						 e.printStackTrace();
					}
			  }
			  System.out.println("Non-daemon thread finished its work");
		 });
		 // By default threads are non-daemon
		 
		 daemonThread.start();
		 nonDaemonThread.start();
		 
		 System.out.println("Main thread exiting...");
		 // Program won't exit until nonDaemonThread completes, regardless of main thread
		 // But daemonThread will be terminated when all non-daemon threads finish
	}
}