package LocknUnlock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FairAndUnfairLockExample {
    // Shared resource
    private int counter = 0;

    // Fair lock: threads acquire the lock in the order they requested it
    private final Lock fairLock = new ReentrantLock(true);

    // Unfair lock: threads may acquire the lock out of order
    private final Lock unfairLock = new ReentrantLock(false);

    // Method to increment the counter using a fair lock
    public void incrementWithFairLock() {
        // Acquire the fair lock
        fairLock.lock();
        try {
            // Critical section: only one thread can execute this at a time
            counter++;
            System.out.println(Thread.currentThread().getName() + " incremented counter to: " + counter + " using fair lock");
        } finally {
            // Always release the lock in a finally block to ensure it is released even if an exception occurs
            fairLock.unlock();
        }
    }

    // Method to increment the counter using an unfair lock
    public void incrementWithUnfairLock() {
        // Acquire the unfair lock
        unfairLock.lock();
        try {
            // Critical section: only one thread can execute this at a time
            counter++;
            System.out.println(Thread.currentThread().getName() + " incremented counter to: " + counter + " using unfair lock");
        } finally {
            // Always release the lock in a finally block to ensure it is released even if an exception occurs
            unfairLock.unlock();
        }
    }

    public static void main(String[] args) {
        FairAndUnfairLockExample example = new FairAndUnfairLockExample();
        FairAndUnfairLockExample example1 = new FairAndUnfairLockExample();

        // Create multiple threads that will increment the counter using the fair lock
        Thread t1 = new Thread(example::incrementWithFairLock, "Thread-1");
        Thread t2 = new Thread(example1::incrementWithFairLock, "Thread-2");
        Thread t3 = new Thread(example::incrementWithFairLock, "Thread-3");

        // Start the threads
        t1.start();
        t2.start();
        t3.start();

        // Wait for all threads to finish
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Reset counter for unfair lock example
        example.counter = 0;

        // Create multiple threads that will increment the counter using the unfair lock
        Thread t4 = new Thread(example::incrementWithUnfairLock, "Thread-4");
        Thread t5 = new Thread(example::incrementWithUnfairLock, "Thread-5");
        Thread t6 = new Thread(example::incrementWithUnfairLock, "Thread-6");

        // Start the threads
        t4.start();
        t5.start();
        t6.start();

        // Wait for all threads to finish
        try {
            t4.join();
            t5.join();
            t6.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Print the final value of the counter
        System.out.println("Final counter value: " + example.counter);
    }
}