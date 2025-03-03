package DeadLock;

/**
 * DeadlockDemoAndSolutions.java
 * 
 * CONCEPT: Deadlock & Solutions
 * ---------------------------
 * Deadlock occurs when two or more threads each hold resources and wait for resources
 * held by another thread, creating a circular dependency where no thread can proceed.
 * 
 * REAL-WORLD ANALOGY:
 * Two people at a narrow hallway intersection, each trying to get past:
 * - Person A steps halfway into the hall and waits for Person B to back up
 * - Person B steps halfway into the hall and waits for Person A to back up
 * - Both are stuck waiting for the other, and neither can proceed
 */
public class DeadlockDemoAndSolutions {

    // Two resources that will be locked
    private static final Object RESOURCE_A = new Object();
    private static final Object RESOURCE_B = new Object();
    
    public static void main(String[] args) {
        System.out.println("===== DEADLOCK DEMONSTRATION =====");
        
        // Uncomment ONE of these methods at a time to see each example:
        
        demonstrateDeadlock();
        // solution1_OrderedLocking();
        // solution2_TryLock();
        // solution3_Timeout();
        // solution4_LockHierarchy();
        // solution5_ResourceAllocationGraph();
    }
    
    /**
     * Demonstrates a classic deadlock scenario
     */
    private static void demonstrateDeadlock() {
        System.out.println("Running deadlock demonstration...");
        
        // Thread 1 - acquires RESOURCE_A, then tries to acquire RESOURCE_B
        Thread thread1 = new Thread(() -> {
            System.out.println("Thread 1: Attempting to lock Resource A");
            synchronized (RESOURCE_A) {
                System.out.println("Thread 1: Locked Resource A");
                
                // Sleep to ensure the other thread has time to acquire Resource B
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
                System.out.println("Thread 1: Attempting to lock Resource B");
                
                // This will wait forever if Thread 2 is holding Resource B
                synchronized (RESOURCE_B) {
                    System.out.println("Thread 1: Locked Resource B");
                    // Use both resources
                }
            }
            System.out.println("Thread 1: Released all locks");
        }, "Thread-1");
        
        // Thread 2 - acquires RESOURCE_B, then tries to acquire RESOURCE_A
        Thread thread2 = new Thread(() -> {
            System.out.println("Thread 2: Attempting to lock Resource B");
            synchronized (RESOURCE_B) {
                System.out.println("Thread 2: Locked Resource B");
                
                // Sleep to ensure the other thread has time to acquire Resource A
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
                System.out.println("Thread 2: Attempting to lock Resource A");
                
                // This will wait forever if Thread 1 is holding Resource A
                synchronized (RESOURCE_A) {
                    System.out.println("Thread 2: Locked Resource A");
                    // Use both resources
                }
            }
            System.out.println("Thread 2: Released all locks");
        }, "Thread-2");
        
        // Start both threads
        thread1.start();
        thread2.start();
        
        // Wait for threads to finish (they won't in deadlock)
        try {
            thread1.join(5000);  // Wait up to 5 seconds
            thread2.join(5000);  // Wait up to 5 seconds
            
            System.out.println("\nDEADLOCK EXPLANATION:");
            System.out.println("1. Thread 1 acquired Resource A and wants Resource B");
            System.out.println("2. Thread 2 acquired Resource B and wants Resource A");
            System.out.println("3. Neither thread can proceed, and both wait forever");
            System.out.println("4. This is deadlock - a circular wait condition\n");
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * SOLUTION 1: Ordered Lock Acquisition
     * 
     * Always acquire locks in a consistent, predetermined order.
     * If all threads acquire locks in the same order, deadlock cannot occur.
     */
    private static void solution1_OrderedLocking() {
        System.out.println("Running Solution 1: Ordered Locking");
        
        // Both threads acquire resources in the SAME order: A then B
        
        Thread thread1 = new Thread(() -> {
            System.out.println("Thread 1: Following lock order - Resource A first");
            synchronized (RESOURCE_A) {  // Always lock A first
                System.out.println("Thread 1: Locked Resource A");
                
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
                System.out.println("Thread 1: Attempting to lock Resource B");
                synchronized (RESOURCE_B) {  // Then lock B
                    System.out.println("Thread 1: Locked Resource B");
                    // Use both resources
                }
            }
            System.out.println("Thread 1: Released all locks");
        }, "Thread-1");
        
        Thread thread2 = new Thread(() -> {
            System.out.println("Thread 2: Following same lock order - Resource A first");
            synchronized (RESOURCE_A) {  // Always lock A first (SAME order)
                System.out.println("Thread 2: Locked Resource A");
                
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
                System.out.println("Thread 2: Attempting to lock Resource B");
                synchronized (RESOURCE_B) {  // Then lock B
                    System.out.println("Thread 2: Locked Resource B");
                    // Use both resources
                }
            }
            System.out.println("Thread 2: Released all locks");
        }, "Thread-2");
        
        thread1.start();
        thread2.start();
        
        try {
            thread1.join();
            thread2.join();
            System.out.println("\nSolution 1 Explanation:");
            System.out.println("- Both threads acquire locks in the SAME order (A then B)");
            System.out.println("- Thread 2 waits until Thread 1 releases Resource A");
            System.out.println("- This prevents the circular wait condition\n");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * SOLUTION 2: Try-Lock (Lock with Timeout)
     * 
     * Use locks that support timeout, like ReentrantLock with tryLock().
     * If a lock cannot be acquired in a certain time, release all locks and retry.
     */
    private static void solution2_TryLock() {
        System.out.println("Running Solution 2: Try-Lock Pattern");
        System.out.println("Note: This is a pseudocode example showing the concept");
        
        System.out.println("\nReal Implementation would look like:");
        System.out.println("Lock lockA = new ReentrantLock();");
        System.out.println("Lock lockB = new ReentrantLock();");
        System.out.println("boolean gotLockA = false;");
        System.out.println("boolean gotLockB = false;");
        System.out.println("try {");
        System.out.println("    // Try to get first lock with timeout");
        System.out.println("    gotLockA = lockA.tryLock(1, TimeUnit.SECONDS);");
        System.out.println("    if (gotLockA) {");
        System.out.println("        // Got first lock, try second with timeout");
        System.out.println("        gotLockB = lockB.tryLock(1, TimeUnit.SECONDS);");
        System.out.println("    }");
        System.out.println("    if (gotLockA && gotLockB) {");
        System.out.println("        // Use both resources");
        System.out.println("    }");
        System.out.println("} finally {");
        System.out.println("    // Release any locks we got");
        System.out.println("    if (gotLockB) lockB.unlock();");
        System.out.println("    if (gotLockA) lockA.unlock();");
        System.out.println("}");
        
        System.out.println("\nSolution 2 Explanation:");
        System.out.println("- Threads don't wait forever for locks");
        System.out.println("- If a lock can't be acquired within timeout, release");
        System.out.println("- all held locks and try again later");
        System.out.println("- Prevents indefinite waiting\n");
    }
    
    /**
     * SOLUTION 3: Lock Timeout Detection
     * 
     * Set a global timeout for acquiring all needed locks.
     * If timeout expires, release all locks and retry.
     */
    private static void solution3_Timeout() {
        System.out.println("Running Solution 3: Global Timeout");
        System.out.println("Note: This is a pseudocode example showing the concept");
        
        System.out.println("\nConcept implementation:");
        System.out.println("long endTime = System.currentTimeMillis() + 5000; // 5 second timeout");
        System.out.println("while (System.currentTimeMillis() < endTime) {");
        System.out.println("    if (tryToAcquireAllLocks()) {");
        System.out.println("        try {");
        System.out.println("            // Use resources");
        System.out.println("            return; // Success!");
        System.out.println("        } finally {");
        System.out.println("            releaseAllLocks();");
        System.out.println("        }");
        System.out.println("    }");
        System.out.println("    // Wait a bit before retry");
        System.out.println("    Thread.sleep(100);");
        System.out.println("}");
        System.out.println("// Timeout expired without getting all locks");
        
        System.out.println("\nSolution 3 Explanation:");
        System.out.println("- Sets an overall time limit for lock acquisition");
        System.out.println("- Prevents indefinite blocking");
        System.out.println("- Can implement backoff strategies on retry\n");
    }
    
    /**
     * SOLUTION 4: Lock Hierarchy/Numbering
     * 
     * Assign a unique number to each lock and always acquire in numeric order.
     * This is a formalized version of Solution 1.
     */
    private static void solution4_LockHierarchy() {
        System.out.println("Running Solution 4: Lock Hierarchy");
        
        System.out.println("\nConcept implementation:");
        System.out.println("// Assign numbers to resources");
        System.out.println("final Object RESOURCE_1 = new Object(); // ID = 1");
        System.out.println("final Object RESOURCE_2 = new Object(); // ID = 2");
        System.out.println("final Object RESOURCE_3 = new Object(); // ID = 3");
        System.out.println("");
        System.out.println("// Always acquire resources in increasing ID order");
        System.out.println("synchronized(RESOURCE_1) {");
        System.out.println("    synchronized(RESOURCE_2) {");
        System.out.println("        synchronized(RESOURCE_3) {");
        System.out.println("            // Use resources");
        System.out.println("        }");
        System.out.println("    }");
        System.out.println("}");
        
        System.out.println("\nSolution 4 Explanation:");
        System.out.println("- Formal policy that enforces lock ordering");
        System.out.println("- Often implemented with class IDs + object IDs");
        System.out.println("- Eliminates circular wait condition\n");
    }
    
    /**
     * SOLUTION 5: Deadlock Detection (Resource Allocation Graph)
     * 
     * Use a system that tracks all lock acquisitions and requests.
     * If a potential deadlock is detected, release locks and retry.
     */
    private static void solution5_ResourceAllocationGraph() {
        System.out.println("Running Solution 5: Deadlock Detection");
        
        System.out.println("\nConcept explanation:");
        System.out.println("1. Maintain a graph of which threads hold which resources");
        System.out.println("2. Also track which resources each thread is waiting for");
        System.out.println("3. Periodically check for cycles in this graph");
        System.out.println("4. If a cycle is found, break it by releasing locks");
        System.out.println("   from one of the participating threads");
        
        System.out.println("\nSolution 5 Explanation:");
        System.out.println("- Allows detection of potential deadlocks");
        System.out.println("- Can recover from deadlock situations");
        System.out.println("- More complex to implement");
        System.out.println("- Often used in database systems\n");
        
        System.out.println("\nSUMMARY OF DEADLOCK PREVENTION TECHNIQUES:");
        System.out.println("1. Lock Ordering - Always acquire locks in the same global order");
        System.out.println("2. Try-Lock - Use timeouts when acquiring locks");
        System.out.println("3. Global Timeout - Set overall deadline for all lock acquisitions");
        System.out.println("4. Lock Hierarchy - Formalize lock ordering with numbering system");
        System.out.println("5. Deadlock Detection - Monitor lock acquisition to detect cycles");
        System.out.println("\nAdditional approaches:");
        System.out.println("- Lock-free algorithms - Use atomic operations instead of locks");
        System.out.println("- Higher-level concurrency tools - Use queues, barriers, etc.");
        System.out.println("- Thread confinement - Keep data local to threads when possible");
    }
}

/* DEEPER UNDERSTANDING OF DEADLOCK:

   1. Four Necessary Conditions for Deadlock:
      - Mutual Exclusion: Locks are exclusive (only one thread can hold at a time)
      - Hold and Wait: Threads hold resources while waiting for others
      - No Preemption: Locks cannot be forcibly taken from threads
      - Circular Wait: Circular chain of threads waiting for each other
   
   2. Deadlock vs. Livelock:
      - Deadlock: Threads are blocked, waiting forever (complete standstill)
      - Livelock: Threads are active but make no progress (like two people in
        a hallway repeatedly moving side to side trying to let each other pass)
   
   3. Deadlock vs. Starvation:
      - Deadlock: Multiple threads block each other (none can proceed)
      - Starvation: Some threads make progress, others never get resources
   
   4. Best Practices:
      - Minimize nested lock acquisitions
      - Hold locks for the shortest time possible
      - Consider using higher-level concurrency utilities
      - Document lock ordering requirements
      - Use deadlock detection in critical systems
*/