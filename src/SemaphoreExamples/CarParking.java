package SemaphoreExamples;

import java.util.concurrent.Semaphore;

/**
 * This example demonstrates how semaphores can control access to a limited resource.
 * In this case, we simulate a parking lot with a limited number of spaces.
 */
public class CarParking {
    
    public static void main(String[] args) {
        // Create a parking lot with 3 available spaces (permits)
        ParkingLot parkingLot = new ParkingLot(3);
        
        // Create 5 cars that will try to park
        for (int i = 1; i <= 5; i++) {
            Thread carThread = new Thread(new Car(i, parkingLot));
            carThread.start();
        }
    }
    
    /**
     * Represents a parking lot with limited spaces.
     * Uses a semaphore to control access to the parking spaces.
     */
    static class ParkingLot {
        // The semaphore keeps track of available parking spaces
        private final Semaphore semaphore;
        private final int totalSpaces;
        
        public ParkingLot(int spaces) {
            // Initialize semaphore with the number of available parking spaces
            // The 'true' parameter makes this a fair semaphore (first-come, first-served)
            semaphore = new Semaphore(spaces, true);
            totalSpaces = spaces;
        }
        
        /**
         * A car attempts to park in the parking lot.
         * If no spaces are available, the car waits.
         */
        public void parkCar(int carId) {
            try {
                // Print current status of the semaphore
                System.out.printf("Car #%d is trying to park. Available spaces: %d/%d\n", 
                        carId, semaphore.availablePermits(), totalSpaces);
                
                // acquire() will block until a permit is available
                semaphore.acquire();
                
                // If we get here, the car has successfully acquired a parking space
                System.out.printf("Car #%d has PARKED. Available spaces: %d/%d\n", 
                        carId, semaphore.availablePermits(), totalSpaces);
                
                // Car stays parked for a while (simulate with sleep)
                Thread.sleep((long) (Math.random() * 5000) + 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        /**
         * A car leaves the parking lot, freeing up a space.
         */
        public void leaveParkingLot(int carId) {
            // release() increases available permits by one
            semaphore.release();
            
            System.out.printf("Car #%d has LEFT. Available spaces: %d/%d\n", 
                    carId, semaphore.availablePermits(), totalSpaces);
        }
    }
    
    /**
     * Represents a car that will try to park and then leave after some time.
     */
    static class Car implements Runnable {
        private final int carId;
        private final ParkingLot parkingLot;
        
        public Car(int carId, ParkingLot parkingLot) {
            this.carId = carId;
            this.parkingLot = parkingLot;
        }
        
        @Override
        public void run() {
            // The car arrives and tries to park
            parkingLot.parkCar(carId);
            
            // The car leaves after parking for some time
            parkingLot.leaveParkingLot(carId);
        }
    }
}
