package CreatingThreadsExamples.InnerClassExample;

// Explanation:
// Defines an inner class InnerRunnable that implements Runnable.
// The run method contains the code to be executed by the thread.
// The thread is named "Thread-2" and started with t2.start().

public class InnerClassExample {
    public static void main(String[] args) {
        InnerClassExample example = new InnerClassExample();
        example.startInnerClassThread();
    }

    public void startInnerClassThread() {
        // Inner class that implements Runnable
        class InnerRunnable implements Runnable {
            @Override
            public void run() {
                System.out.println("Running in Inner Class");
            }
        }
        // Creating and starting the thread with the inner class
        Thread t2 = new Thread(new InnerRunnable(), "Thread-2");
        t2.start();
    }
}