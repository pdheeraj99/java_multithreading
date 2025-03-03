package Join_vs_CountDownLatch;

/**
 * CountDownLatchWithExecutorDemo.java - CountDownLatch with ExecutorService
 * 
 * CONCEPT: CountDownLatch with Executor Framework
 * ----------------------------------------------
 * This demonstrates how to use CountDownLatch with the Executor framework for 
 * better thread management. The ExecutorService manages a pool of worker threads, 
 * while the CountDownLatch coordinates when the tasks are complete.
 * 
* REAL-WORLD ANALOGY (VERY SIMPLE): 
 * 
 * Imagine you're planning a house move:
 * 
 * 1. You have 4 boxes that need to be packed (these are the TASKS)
 *    - Box 1: Kitchen stuff
 *    - Box 2: Bedroom stuff
 *    - Box 3: Bathroom stuff
 *    - Box 4: Living room stuff
 * 
 * 2. You have only 2 friends helping you pack (this is the THREAD POOL)
 *    - Friend A
 *    - Friend B
 * 
 * 3. You have a checklist with 4 items (this is the COUNTDOWN LATCH)
 *    - Kitchen: □ Not packed yet
 *    - Bedroom: □ Not packed yet
 *    - Bathroom: □ Not packed yet
 *    - Living room: □ Not packed yet
 * 
 * How it works:
 * - You (main thread) tell your 2 friends what needs to be packed
 * - Friend A starts packing the kitchen box
 * - Friend B starts packing the bedroom box
 * - When Friend A finishes the kitchen box, they check it off: ✓
 * - Then Friend A starts on the bathroom box
 * - When Friend B finishes the bedroom box, they check it off: ✓
 * - Then Friend B starts on the living room box
 * - Each friend checks off boxes as they finish them: ✓
 * - You (main thread) are waiting and won't call the moving truck
 *   until all 4 boxes are checked off
 * - Once all boxes are checked, you call the moving truck and proceed
 * 
 * Key points:
 * - Tasks = Boxes that need packing (4 total)
 * - Thread pool = Your 2 friends who do the work
 * - CountDownLatch = The checklist of 4 boxes
 * - Main thread = You waiting for everything to be done
 * 
 * Notice how 2 friends can pack 4 boxes by working one after another!
 */

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * A simple moving day example that demonstrates CountDownLatch with ExecutorService.
 * We have 4 boxes to pack but only 2 helpers (threads) to do the packing.
 */
public class CountDownLatchWithExecutorDemo {

    public static void main(String[] args) {
        // We have 4 boxes to pack
        String[] boxes = {
            "Kitchen Box", 
            "Bedroom Box", 
            "Bathroom Box", 
            "Living Room Box"
        };
        
        // Our checklist has 4 items to check off
        CountDownLatch packingDone = new CountDownLatch(boxes.length);
        
        // We have 2 friends helping us
        ExecutorService helpers = Executors.newFixedThreadPool(2);
        
        System.out.println("Moving day has started! 4 boxes need packing");
        
        // Give each box to our helpers to pack
        for (String boxName : boxes) {
            helpers.submit(() -> {
                try {
                    String helperName = Thread.currentThread().getName();
                    
                    System.out.println(helperName + " starts packing: " + boxName);
                    
                    // Simulate time it takes to pack a box (random time between 1-3 seconds)
                    Thread.sleep((long)(Math.random() * 2000) + 1000);
                    
                    System.out.println(helperName + " finished packing: " + boxName);
                    
                    // Check off this box from our checklist
                    packingDone.countDown();
                    
                    System.out.println("Boxes left to pack: " + packingDone.getCount());
                    
                } catch (InterruptedException e) {
                    System.out.println("Helper was interrupted while packing");
                }
            });
        }
        
        try {
            System.out.println("I'm waiting for all boxes to be packed before calling the moving truck...");
            
            // Wait until all boxes are packed (our checklist is complete)
            packingDone.await();
            
            System.out.println("All boxes are packed! Now I can call the moving truck!");
            
        } catch (InterruptedException e) {
            System.out.println("I was interrupted while waiting for packing to finish");
        } finally {
            System.out.println("Thanking our helpers and saying goodbye");
            
            // Tell our helpers they can go home
            helpers.shutdown();
            
            try {
                // Give them a few seconds to gather their things
                if (!helpers.awaitTermination(2, TimeUnit.SECONDS)) {
                    // If they're taking too long, insist they leave now
                    helpers.shutdownNow();
                }
            } catch (InterruptedException e) {
                helpers.shutdownNow();
            }
        }
        
        System.out.println("Moving day completed!");
    }
}