package Lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockExampleWithReentrantLock {
    // Shared resource
    private int counter = 0;

    // Lock to control access to the shared resource
    private final Lock lock = new ReentrantLock();

    // Method to increment the counter safely
    public void increment() {
        // Acquire the lock
        lock.lock();
        try {
            // Critical section: only one thread can execute this at a time
            counter++;
            System.out.println(Thread.currentThread().getName() + " incremented counter to: " + counter);
        } finally {
            // Always release the lock in a finally block to ensure it is released even if an exception occurs
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        LockExampleWithReentrantLock example = new LockExampleWithReentrantLock();

        // Create multiple threads that will increment the counter
        Thread t1 = new Thread(example::increment, "Thread-1");
        Thread t2 = new Thread(example::increment, "Thread-2");
        Thread t3 = new Thread(example::increment, "Thread-3");

        // Start the threads
        t1.start();
        t2.start();
        t3.start();
    }
}