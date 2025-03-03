package DaemonThreads;

// REAL WORLD ANALOGY

public class AutoSaveExample {
	public static void main(String[] args) {
		 // Simulate document content
		 StringBuilder document = new StringBuilder("Initial document content");
		 
		 // Auto-save daemon thread - like a background auto-save feature in a text editor
		 Thread autoSaveThread = new Thread(() -> {
			  int saveCount = 0;
			  while (true) {
					saveCount++;
					System.out.println("[Auto-save] Saving document: " + document.toString());
					System.out.println("[Auto-save] Save count: " + saveCount);
					
					try {
						 // Auto-save every 2 seconds
						 Thread.sleep(2000);
					} catch (InterruptedException e) {
						 System.out.println("[Auto-save] Auto-save interrupted");
						 return;
					}
			  }
		 });
		 
		 // Set as daemon - when application closes, auto-save stops
		 autoSaveThread.setDaemon(true);
		 autoSaveThread.start();
		 
		 // Main thread simulates user editing document
		 for (int i = 1; i <= 5; i++) {
			  try {
					// User makes changes every second
					Thread.sleep(1000);
					document.append(" - Edit " + i);
					System.out.println("[User] Made edit #" + i + ": " + document.toString());
			  } catch (InterruptedException e) {
					e.printStackTrace();
			  }
		 }
		 
		 System.out.println("[Application] User closed the editor, exiting...");
		 // When main exits, the auto-save daemon will be terminated
	}
}