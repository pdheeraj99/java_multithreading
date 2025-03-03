# Semaphore in Java

## Definition
A Semaphore is a synchronization mechanism that maintains a set of permits that can be acquired and released by threads. It's used to control access to a shared resource with limited capacity.

## Key Points
1. Initialize with a number of permits (positive integer)
2. `acquire()` - Gets a permit (blocks if none available)
3. `release()` - Returns a permit to the semaphore
4. Can act as a mutex (permit=1) or resource counter (permit>1)
5. Supports fairness option (first-come, first-served)
6. Used for resource pooling and rate limiting

## Simple Code Example

```java
import java.util.concurrent.Semaphore;

/**
 * SemaphoreExample - Controlling access to limited resources
 * 
 * This example simulates a parking lot with limited spaces.
 * Cars can only enter when spaces are available.
 */
public class SemaphoreExample {
    public static void main(String[] args) {
        // Create a parking lot with 3 spaces
        ParkingLot parkingLot = new ParkingLot(3);
        
        // Create 5 cars that will try to park
        for (int i = 1; i <= 5; i++) {
            new Thread(new Car(i, parkingLot)).start();
        }
    }
}

class ParkingLot {
    // Semaphore controls access to parking spaces
    private final Semaphore spaces;
    
    public ParkingLot(int capacity) {
        // Initialize with number of parking spaces
        // true = fair mode (FIFO order)
        spaces = new Semaphore(capacity, true);
        System.out.println("Parking lot created with " + capacity + " spaces");
    }
    
    public void park(int carId) throws InterruptedException {
        // Try to acquire a parking space
        System.out.println("Car #" + carId + " is waiting to park");
        
        // KEY METHOD: Will block if no spaces available
        spaces.acquire();
        
        // If we get here, the car has a parking space
        System.out.println("Car #" + carId + " has PARKED. " +
                          "Spaces left: " + spaces.availablePermits());
    }
    
    public void leave(int carId) {
        // Release the space when car leaves
        spaces.release();
        
        System.out.println("Car #" + carId + " has LEFT. " +
                          "Spaces available: " + spaces.availablePermits());
    }
}

class Car implements Runnable {
    private final int id;
    private final ParkingLot parkingLot;
    
    public Car(int id, ParkingLot parkingLot) {
        this.id = id;
        this.parkingLot = parkingLot;
    }
    
    @Override
    public void run() {
        try {
            // Try to park
            parkingLot.park(id);
            
            // Parked for some time
            Thread.sleep((long)(Math.random() * 2000) + 1000);
            
            // Leave parking lot
            parkingLot.leave(id);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
```

## Key Methods
1. `acquire()` - Gets a permit, blocks if none
2. `release()` - Returns a permit
3. `tryAcquire()` - Tries to get a permit without blocking
4. `availablePermits()` - Returns number of available permits

## Common Use Cases
1. Limiting database connections
2. Thread pool management
3. Rate limiting API calls
4. Managing access to physical resources

## How It Works
- When semaphore is created with N permits, N threads can acquire at once
- The N+1th thread will block at `acquire()` until a permit is released
- Each `release()` adds one permit back to the semaphore