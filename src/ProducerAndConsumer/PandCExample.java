package ProducerAndConsumer;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * PandCExample - Producer Consumer Pattern with ArrayBlockingQueue
 * 
 * CONCEPT: Producer-Consumer with ArrayBlockingQueue
 * -------------------------------------------------
 * This example demonstrates the producer-consumer pattern using Java's built-in
 * ArrayBlockingQueue, which is a thread-safe, bounded, blocking queue that
 * handles all the synchronization details for us.
 * 
 * KEY FEATURES OF ArrayBlockingQueue:
 * 1. Thread-safe: All operations are atomic and visible to other threads
 * 2. Bounded: Has a fixed capacity specified at construction time
 * 3. Blocking: put() blocks when full, take() blocks when empty
 * 4. FIFO ordering: First-in, first-out - items are processed in order
 * 
 * REAL-WORLD ANALOGY:
 * 
 * Think of a bakery with a display case that holds exactly 5 pastries:
 * - Baker (producer) makes pastries and places them in the case
 * - Cashier (consumer) takes pastries from the case to sell to customers
 * - If the case is full (5 pastries), the baker must wait before adding more
 * - If the case is empty, the cashier must wait for the baker to add pastries
 * - The ArrayBlockingQueue handles all the waiting and notification automatically
 */
public class PandCExample {
    
    public static void main(String[] args) {
        // Create a bounded blocking queue with capacity of 5
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(2);
        
        // Create and start the producer thread
        Thread producer = new Thread(new Producer(queue), "Producer");
        
        // Create and start the consumer thread
        Thread consumer = new Thread(new Consumer(queue), "Consumer");
        
        // Start both threads
        producer.start();
        consumer.start();
        
        // Wait for both threads to complete
      //   try {
      //       producer.join();
      //       consumer.join();
      //       System.out.println("Demo completed successfully!");
      //   } catch (InterruptedException e) {
      //       Thread.currentThread().interrupt();
      //       System.err.println("Main thread interrupted: " + e.getMessage());
      //   }
    }
}

/**
 * Producer class that generates items and puts them into the shared queue.
 */
class Producer implements Runnable {
    private final BlockingQueue<Integer> queue;
    
    public Producer(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }
    
    @Override
    public void run() {
        try {
            // Produce 10 items (0-9)
            for (int i = 0; i < 10; i++) {
                System.out.println("[" + Thread.currentThread().getName() + "] Producing item: " + i);
                
                // KEY METHOD: put() will block if the queue is full
                // The thread will wait here until there's space in the queue
                queue.put(i);
                
                System.out.println("[" + Thread.currentThread().getName() + 
                                   "] Produced item: " + i + 
                                   " - Queue size: " + queue.size());
                
                // Simulate varying production time
                Thread.sleep((long) (Math.random() * 800));
            }
            
            System.out.println("[" + Thread.currentThread().getName() + "] Production complete!");
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("[" + Thread.currentThread().getName() + 
                               "] Interrupted: " + e.getMessage());
        }
    }
}

/**
 * Consumer class that takes items from the shared queue and processes them.
 */
class Consumer implements Runnable {
    private final BlockingQueue<Integer> queue;
    
    public Consumer(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }
    
    @Override
    public void run() {
        try {
            // Consume 10 items (matching what the producer creates)
            for (int i = 0; i < 10; i++) {
                System.out.println("[" + Thread.currentThread().getName() + "] Waiting to consume...");
                
                // KEY METHOD: take() will block if the queue is empty
                // The thread will wait here until an item is available
                int item = queue.take();
                
                System.out.println("[" + Thread.currentThread().getName() + 
                                   "] Consumed item: " + item + 
                                   " - Queue size: " + queue.size());
                
                // Simulate varying consumption time (slightly slower than producer)
                Thread.sleep((long) (Math.random() * 1200));
            }
            
            System.out.println("[" + Thread.currentThread().getName() + "] Consumption complete!");
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("[" + Thread.currentThread().getName() + 
                               "] Interrupted: " + e.getMessage());
        }
    }
}

/* DEEPER UNDERSTANDING OF ArrayBlockingQueue:

   1. Key Methods:
      - put(E e): Inserts an element, waiting if necessary for space to become available
      - take(): Retrieves and removes the head of the queue, waiting if necessary until an element becomes available
      - offer(E e): Inserts if possible, returns false if queue is full (non-blocking)
      - poll(): Retrieves and removes the head of the queue, or returns null if queue is empty (non-blocking)
      - peek(): Retrieves but does not remove the head of the queue, or returns null if queue is empty
   
   2. Benefits Over Manual Implementation:
      - Eliminates boilerplate synchronization code
      - Avoids common threading bugs
      - Better performance with optimized implementation
      - Clear, expressive API that communicates intent
   
   3. How It Works Internally:
      - Uses a circular array to store elements
      - Uses locks (ReentrantLock) for thread safety
      - Uses condition variables for waiting and notification
      - Maintains count, takeIndex and putIndex to track positions
   
   4. When to Use ArrayBlockingQueue vs LinkedBlockingQueue:
      - ArrayBlockingQueue: Fixed size, slightly better performance with less memory overhead
      - LinkedBlockingQueue: Can be unbounded, better for variable-sized workloads
   
   5. Common Patterns:
      - Multiple producers, single consumer
      - Single producer, multiple consumers
      - Multiple producers, multiple consumers
*/