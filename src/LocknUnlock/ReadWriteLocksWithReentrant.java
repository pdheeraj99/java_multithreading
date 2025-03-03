package LocknUnlock;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLocksWithReentrant {
    private double balance;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public ReadWriteLocksWithReentrant(double initialBalance) {
        this.balance = initialBalance;
    }

    // Method to get the current balance (read operation)
    public double getBalance() {
        lock.readLock().lock(); // Acquire the read lock
        try {
            return balance;
        } finally {
            lock.readLock().unlock(); // Release the read lock
        }
    }

    // Method to deposit money (write operation)
    public void deposit(double amount) {
        lock.writeLock().lock(); // Acquire the write lock
        try {
            if (amount > 0) {
                balance += amount;
                System.out.println("Deposited: " + amount + ", New Balance: " + balance);
            }
        } finally {
            lock.writeLock().unlock(); // Release the write lock
        }
    }

    // Method to withdraw money (write operation)
    public void withdraw(double amount) {
        lock.writeLock().lock(); // Acquire the write lock
        try {
            if (amount > 0 && amount <= balance) {
                balance -= amount;
                System.out.println("Withdrew: " + amount + ", New Balance: " + balance);
            } else {
                System.out.println("Withdrawal of " + amount + " failed. Insufficient funds.");
            }
        } finally {
            lock.writeLock().unlock(); // Release the write lock
        }
    }

    public static void main(String[] args) {
        ReadWriteLocksWithReentrant account = new ReadWriteLocksWithReentrant(1000.0);

        // Creating threads for concurrent access
        Thread t1 = new Thread(() -> {
            account.deposit(500);
            System.out.println("Balance after deposit: " + account.getBalance());
        });

        Thread t2 = new Thread(() -> {
            account.withdraw(200);
            System.out.println("Balance after withdrawal: " + account.getBalance());
        });

        Thread t3 = new Thread(() -> {
            System.out.println("Balance enquiry: " + account.getBalance());
        });

        // Start the threads
        t1.start();
        t2.start();
        t3.start();

        // Wait for threads to finish
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}