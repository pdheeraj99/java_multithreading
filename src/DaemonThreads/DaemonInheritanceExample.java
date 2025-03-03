package DaemonThreads;

public class DaemonInheritanceExample {
	public static void main(String[] args) {
		 // Main thread is non-daemon by default
		 System.out.println("Main thread is daemon: " + Thread.currentThread().isDaemon());
		 
		 // Create a non-daemon thread
		 Thread nonDaemonParent = new Thread(() -> {
			  System.out.println("Non-daemon parent is daemon: " + Thread.currentThread().isDaemon());
			  
			  // Child thread inherits daemon status (will be non-daemon)
			  Thread childOfNonDaemon = new Thread(() -> {
					System.out.println("Child of non-daemon is daemon: " + 
											 Thread.currentThread().isDaemon());
			  });
			  childOfNonDaemon.start();
			  
			  try {
					childOfNonDaemon.join();
			  } catch (InterruptedException e) {
					e.printStackTrace();
			  }
		 });
		 
		 // Create a daemon thread
		 Thread daemonParent = new Thread(() -> {
			  System.out.println("Daemon parent is daemon: " + Thread.currentThread().isDaemon());
			  
			  // Child thread inherits daemon status (will be daemon)
			  Thread childOfDaemon = new Thread(() -> {
					System.out.println("Child of daemon is daemon: " + 
											 Thread.currentThread().isDaemon());
			  });
			  childOfDaemon.start();
			  
			  try {
					childOfDaemon.join();
			  } catch (InterruptedException e) {
					e.printStackTrace();
			  }
		 });
		 
		 daemonParent.setDaemon(true);
		 
		 nonDaemonParent.start();
		 daemonParent.start();
		 
		 try {
			  nonDaemonParent.join();
			  daemonParent.join();
		 } catch (InterruptedException e) {
			  e.printStackTrace();
		 }
	}
}
