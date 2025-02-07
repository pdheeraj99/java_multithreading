package CreatingThreadsWithRunnableExamples.NamedClassExample;

// Explanation:
// Defines a separate named class NamedRunnable that implements Runnable.
// The run method contains the code to be executed by the thread.
// The thread is named "Thread-3" and started with t3.start().

public class NamedClassExample {
    public static void main(String[] args) {
        Thread t3 = new Thread(new NamedRunnable(), "Thread-3");
        t3.start();
    }
}

// Named class that implements Runnable
class NamedRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("Running in Named Class");
    }
}