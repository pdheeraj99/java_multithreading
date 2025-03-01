package Synchronization;

public class SynchronizedMethodVsBlockDemo {
        private int counter = 0;

        // For entire method we are keeping lock for necessary and unnecessary code
        // Synchronized method: The entire method is synchronized, meaning only one thread can execute it at a time.
        public synchronized void incrementCounter() {
            counter++;
            System.out.println("Counter (synchronized method): " + counter);
        }

        // Only what is required we will keep in block and then only it will apply lock
        // Method with synchronized block: Only the code inside the synchronized block is synchronized.
        public void incrementCounterWithBlock() {
            System.out.println("Hello 1");
            System.out.println("Hello 2");
            // Synchronized block: Only this section of code is synchronized, allowing finer control over synchronization.
            synchronized (this) {
                counter++;
                System.out.println("Counter (synchronized block): " + counter);
            }
        }

        public static void main(String[] args) {
            SynchronizedMethodVsBlockDemo example = new SynchronizedMethodVsBlockDemo();

            // Create a thread using an ANONYMOUS INNER CLASS implementing Runnable
            Thread thread1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 5; i++) {
                        example.incrementCounter();
                    }
                }
            });

            // Create a thread using a LAMBDA EXPRESSION
            // Lambdas we can write for interface with one abstract method which we call it as FUNCTIONAL INTERFACE
            Thread thread2 = new Thread(() -> {
                for (int i = 0; i < 5; i++) {
                    example.incrementCounterWithBlock();
                }
            });

//---------------------------------------------------------
//            NAMED INNER CLASS

//            public class MyRunnable implements Runnable {
//                @Override
//                public void run() {
//                    for (int i = 0; i < 5; i++) {
//                        example.incrementCounter();
//                    }
//                }
//            }
//
//// Creating a thread using the named inner class
//            Thread thread1 = new Thread(new MyRunnable());
//-----------------------------------------------------------


            // Start the threads
            thread1.start();
            thread2.start();

            // Wait for threads to finish
            try {
                thread1.join();
                thread2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

}