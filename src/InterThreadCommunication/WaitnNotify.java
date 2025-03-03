package InterThreadCommunication;

/**
 * WaitNotifyDemo - Thread communication using wait() and notify()
 * 
 * CONCEPT: Inter-Thread Communication with wait()/notify()
 * -----------------------------------------------------
 * The wait() and notify() methods allow threads to communicate about the 
 * state of a shared resource and coordinate their actions.
 * 
 * KEY POINTS:
 * - wait() releases the lock and puts thread in a waiting state
 * - notify() wakes up one waiting thread
 * - notifyAll() wakes up all waiting threads
 * - These methods must be called from a synchronized context
 * - They are methods of Object class, not Thread class
 * 
 * REAL-WORLD ANALOGY:
 * 
 * Think of a coffee shop:
 * - Barista (producer thread) makes coffee
 * - Customer (consumer thread) waits for their coffee
 * - When customer places order, they "wait()" at the pickup counter
 * - When barista completes the order, they "notify()" the customer
 * - The customer then wakes up and takes their coffee
 */
public class WaitnNotify {
    
    public static void main(String[] args) {
        // Shared data object that both threads will use
        Message message = new Message();
        
        // Create and start the receiver thread (will wait for message)
        Thread receiverThread = new Thread(new MessageReceiver(message), "Receiver");
        receiverThread.start();
        
        // Give receiver time to start and enter waiting state
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Create and start the sender thread (will set message and notify)
        Thread senderThread = new Thread(new MessageSender(message), "Sender");
        senderThread.start();
        
        // Wait for both threads to finish
        try {
            receiverThread.join();
            senderThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("Main: Demo completed!");
    }
}

/**
 * Shared data object that holds the message being passed between threads.
 */
class Message {
    private String content;
    private boolean empty = true;  // Flag indicating if message is empty
    
    /**
     * Get the message - called by the receiver thread.
     * Will wait if the message is empty.
     */
    public synchronized String read() {
        // While there is no message to read, wait
        while (empty) {
            try {
                System.out.println(Thread.currentThread().getName() + 
                                   ": No message yet. Waiting...");
                
                // KEY METHOD: wait() releases the lock and puts thread in waiting state
                // This thread will not continue until another thread calls notify()
                wait();  // Releases lock on 'this' object
                
                // When notify() is called, thread reacquires lock and continues from here
                System.out.println(Thread.currentThread().getName() + 
                                   ": Notified! Continuing execution");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Interrupted while waiting: " + e.getMessage());
                return null;
            }
        }
        
        // Message is available, change state and return message
        empty = true;  // Mark as empty so sender can send another message
        
        System.out.println(Thread.currentThread().getName() + 
                           ": Received message: " + content);
        
        // KEY METHOD: notify() wakes up a waiting thread
        // This signals that the message has been read
        notify();  // Let any waiting thread know we've read the message
        
        return content;
    }
    
    /**
     * Set the message - called by the sender thread.
     * Will wait if the previously sent message hasn't been read yet.
     */
    public synchronized void write(String message) {
        // While there is still an unread message, wait
        while (!empty) {
            try {
                System.out.println(Thread.currentThread().getName() + 
                                   ": Previous message not read yet. Waiting...");
                
                // Wait until the receiver reads the message and sets empty=true
                wait();  // Releases lock on 'this' object
                
                System.out.println(Thread.currentThread().getName() + 
                                   ": Notified! Continuing to send");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Interrupted while waiting: " + e.getMessage());
                return;
            }
        }
        
        // Previous message has been read, so we can write new one
        empty = false;  // Mark as not empty
        this.content = message;
        
        System.out.println(Thread.currentThread().getName() + 
                           ": Sent message: " + message);
        
        // Notify any waiting thread (the receiver) that a message is available
        notify();
    }
}

/**
 * The MessageReceiver waits for messages and reads them when available.
 */
class MessageReceiver implements Runnable {
    private final Message message;
    
    public MessageReceiver(Message message) {
        this.message = message;
    }
    
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + ": Started");
        
        // Read 5 messages
        for (int i = 0; i < 5; i++) {
            String receivedMessage = message.read();
            
            // Process the message (here we just sleep a bit)
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        System.out.println(Thread.currentThread().getName() + ": Finished");
    }
}

/**
 * The MessageSender sends several messages.
 */
class MessageSender implements Runnable {
    private final Message message;
    
    public MessageSender(Message message) {
        this.message = message;
    }
    
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + ": Started");
        
        // Send 5 messages
        String[] messages = {
            "Hello!",
            "How are you?",
            "Wait/notify is interesting",
            "This is message #4",
            "Goodbye!"
        };
        
        for (String msg : messages) {
            message.write(msg);
            
            // Small delay between sends
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        System.out.println(Thread.currentThread().getName() + ": Finished");
    }
}

/* DEEPER UNDERSTANDING OF wait()/notify():

   1. Why use while loop instead of if for wait conditions?
      - Protects against spurious wakeups (when a thread wakes up without notification)
      - Re-checks the condition after wakeup to ensure it's truly satisfied
      - Handles the case where multiple threads might be competing
   
   2. Key Rules for wait()/notify():
      - Must be called from a synchronized context (method or block)
      - Must be called on the object whose lock the thread holds
      - wait() releases the lock while waiting
      - After notify(), the notified thread still needs to reacquire the lock
   
   3. Common Pitfalls:
      - Calling wait() without checking a condition (use a while loop)
      - Forgetting to call notify() when state changes
      - Calling wait()/notify() outside of synchronized context
      - Using Thread.sleep() instead of wait() (sleep doesn't release the lock)
   
   4. notify() vs notifyAll():
      - notify(): Wakes up a single waiting thread (arbitrary choice)
      - notifyAll(): Wakes up all waiting threads
      - notifyAll() is safer but less efficient
      - Use notify() when exactly one thread should proceed
      - Use notifyAll() when multiple threads may need to reevaluate conditions
   
   5. Alternative Approaches:
      - Condition objects from Lock interface
      - BlockingQueue for producer-consumer patterns
      - CountDownLatch, CyclicBarrier for synchronization points
      - CompletableFuture for asynchronous operations
*/
