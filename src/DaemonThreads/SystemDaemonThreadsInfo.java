package DaemonThreads;

public class SystemDaemonThreadsInfo {
	public static void main(String[] args) {
		 // Display information about system daemon threads
		 System.out.println("System daemon threads currently running:");
		 System.out.println("----------------------------------------");
		 
		 // Get the root thread group
		 ThreadGroup rootGroup = Thread.currentThread().getThreadGroup();
		 ThreadGroup parentGroup;
		 while ((parentGroup = rootGroup.getParent()) != null) {
			  rootGroup = parentGroup;
		 }
		 
		 // Estimate thread count (might be more by the time we query)
		 int estimatedCount = rootGroup.activeCount();
		 Thread[] threads = new Thread[estimatedCount * 2]; // Double the size to be safe
		 
		 // Get all threads
		 int actualCount = rootGroup.enumerate(threads, true);
		 
		 // Print info for daemon threads
		 int daemonCount = 0;
		 for (int i = 0; i < actualCount; i++) {
			  Thread thread = threads[i];
			  if (thread.isDaemon()) {
					daemonCount++;
					System.out.println(daemonCount + ". " + thread.getName() + 
											" (Priority: " + thread.getPriority() + ")");
					
					// Add descriptions of common daemon threads
					switch (thread.getName()) {
						 case "Finalizer":
							  System.out.println("   - Responsible for executing object finalize() methods");
							  break;
						 case "Reference Handler":
							  System.out.println("   - Handles reference objects queued for processing");
							  break;
						 case "Signal Dispatcher":
							  System.out.println("   - Dispatches signals to JVM and application");
							  break;
						 case "Attach Listener":
							  System.out.println("   - Enables dynamic attachment of tools like profilers");
							  break;
						 case "Common-Cleaner":
							  System.out.println("   - Handles cleanup of direct ByteBuffers and other resources");
							  break;
						 default:
							  if (thread.getName().contains("GC")) {
									System.out.println("   - Garbage Collection thread");
							  }
					}
			  }
		 }
		 
		 System.out.println("\nTotal daemon threads: " + daemonCount);
		 System.out.println("Total threads: " + actualCount);
		 
		 // Important facts about system daemon threads
		 System.out.println("\nKey facts about system daemon threads:");
		 System.out.println("1. JVM can exit when only daemon threads remain");
		 System.out.println("2. Daemon threads are abruptly terminated when JVM exits");
		 System.out.println("3. They are used for background services that don't need graceful shutdown");
		 System.out.println("4. Ideal for garbage collection, housekeeping, caching, etc.");
		 System.out.println("5. Not suitable for tasks requiring reliable completion");
	}
}