package DaemonThreads;

public class GarbageCollectionAnalogy {
	public static void main(String[] args) {
		 // Garbage collector in Java is actually a daemon thread!
		 // This example simulates how a daemon thread can perform clean-up tasks
		 
		 // Create a daemon thread for memory management (similar to GC)
		 Thread memoryManager = new Thread(() -> {
			  while (true) {
					try {
						 // Check for unused objects periodically
						 System.out.println("[Memory Manager] Scanning for unused objects...");
						 Thread.sleep(1000);
						 System.out.println("[Memory Manager] Freeing memory from unused objects");
					} catch (InterruptedException e) {
						 e.printStackTrace();
					}
			  }
		 });
		 
		 // Set as daemon - just like the real garbage collector
		 memoryManager.setDaemon(true);
		 memoryManager.start();
		 
		 // Create objects in main thread
		 for (int i = 0; i < 3; i++) {
			  // Create a large object that might need cleanup
			  byte[] largeArray = new byte[100_000];
			  System.out.println("Created large object #" + i);
			  
			  try {
					Thread.sleep(2000);
			  } catch (InterruptedException e) {
					e.printStackTrace();
			  }
			  
			  // Let the array go out of scope 
			  System.out.println("Object #" + i + " is no longer referenced");
		 }
		 
		 System.out.println("Application completed, exiting...");
		 // Memory manager daemon thread will be terminated when JVM exits
	}
}
